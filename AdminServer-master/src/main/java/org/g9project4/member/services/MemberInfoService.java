package org.g9project4.member.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.g9project4.adminmember.controllers.MemberForm;
import org.g9project4.adminmember.controllers.MemberSearch;
import org.g9project4.adminmember.controllers.RequestMember;
import org.g9project4.adminmember.exceptions.MemberNotFoundException;
import org.g9project4.board.entities.Board;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.member.MemberInfo;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.entities.Authorities;
import org.g9project4.member.entities.Member;
import org.g9project4.member.entities.QMember;
import org.g9project4.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final HttpServletRequest request;
    private final EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        List<Authorities> tmp = Objects.requireNonNullElse(member.getAuthorities(),//getAuthorities 가 Null 이면 뒤에 반환
                List.of(Authorities.builder()
                        .member(member)
                        .authority(Authority.USER)
                        .build()));

        List<SimpleGrantedAuthority> authorities = tmp.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().name()))
                .toList();

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(authorities)
                .member(member)
                .build();
    }
    public Member get(Long seq){
        Member member = memberRepository.findById(seq).orElseThrow(MemberNotFoundException::new);
        return member;
    }
    public Member get(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        return member;
    }
    public RequestMember getForm(Long seq){
        Member member = memberRepository.findById(seq).orElseThrow(MemberNotFoundException::new);
        RequestMember form = new ModelMapper().map(member, RequestMember.class);
        form.setMode("edit");
        return form;
    }
    public RequestMember getForm(String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        RequestMember form = new ModelMapper().map(member, RequestMember.class);
        form.setMode("edit");
        return form;
    }

    public ListData<Member> getList(MemberSearch search){
        int page = Math.max(search.getPage(), 1);
        int limit = Math.max(search.getLimit(), 20);
        int offset = (page - 1) * limit;
        BooleanBuilder andBuilder = new BooleanBuilder();
        QMember member = QMember.member;

        PathBuilder<Member> pathBuilder = new PathBuilder<>(Member.class, "member");

        List<Member> items = new JPAQueryFactory(em)
                .selectFrom(member)
                .leftJoin(member.authorities)
                .fetchJoin()
                .where(andBuilder)
                .limit(limit)
                .offset(offset)
                .orderBy(new OrderSpecifier(Order.DESC,pathBuilder.get("createdAt")))
                .fetch();

        int total = (int)memberRepository.count(andBuilder);
        Pagination pagination = new Pagination(page, total, 10 ,limit , request);
        return new ListData<>(items, pagination);

    }

    public List<Member> searchMember(MemberSearch search){
        BooleanBuilder builder = new BooleanBuilder();
        QMember member = QMember.member;
        if("email".equals(search.getSopt())) {
            builder.and(member.email.like("%" + search.getSkey() + "%"));
        }  else if ("userName".equals(search.getSopt())) {
            builder.and(member.userName.like("%" + search.getSkey() + "%"));
        }  else if ("ALL".equals(search.getSopt())) {
                builder.or(member.email.like("%" + search.getSkey() + "%"))
                        .or(member.userName.like("%" + search.getSkey() + "%"));
        }
        return  new JPAQueryFactory(em)
                .selectFrom(member)
                .leftJoin(member.authorities)
                .fetchJoin()
                .where(builder)
                .fetch();
    }
    public MemberForm getMemberForm(Long seq){
        Member member = memberRepository.findById(seq).orElseThrow(MemberNotFoundException::new);
        MemberForm form = new ModelMapper().map(member, MemberForm.class);
        List<String> authrities = member.getAuthorities().stream().map(a -> a.getAuthority().name()).toList();
        form.setAuthorities(authrities);
        form.setActivity(true);
        return form;
    }
}

