package org.g9project4.adminmember.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.g9project4.adminmember.controllers.MemberSearch;
import org.g9project4.adminmember.controllers.RequestMember;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.member.MemberInfo;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.entities.Authorities;
import org.g9project4.member.entities.Member;
import org.g9project4.member.entities.QMember;
import org.g9project4.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class AllMemberConfigInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final EntityManager em;
    private final HttpServletRequest request;

    /* 회원 정보가 필요할때마다 호출되는 메서드 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//유저의 정보를 불러와서 UserDetails로 리턴

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)); //회원 없을 경우 예외 발생

        /*
        MemberInfo쪽에 getAuthorities()메서드를 통해서 사용자 권한 조회,
        권한이 null이거나 비어있을 때 대체 할 기본권한 -> USER, null이 아닌 경우 기존 권한 그대로 반환
         */
        List<Authorities> tmp = member.getAuthorities();
        if(tmp == null || tmp.isEmpty()){
            tmp = List.of(Authorities.builder().member(member).authority(Authority.USER).build());
        }

        /*
        tmp에서 가져온 Authorities 객체 리스트를 Spring Security가 이해할 수 있는 SimpleGrantedAuthority 객체 리스트로 변환하는 가공 단계가 필요하다.
         */
        List<SimpleGrantedAuthority> authorities = tmp.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
                .toList();//Authority enum의 name 메서드를 호출하여 문자열로 변환해야한다.(authority는 enum상수로 되어있기 때문!)

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }

    public Member get(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return member;
    }


    /**
     * 회원 목록
     *
     * @param search
     * @return
     */
    public ListData<Member> getList(MemberSearch search) {

        int page = Math.max(search.getPage(), 1); // 페이지 번호
        int limit = Math.max(search.getLimit(), 10); // 1페이지당 레코드 갯수
        int offset = (page - 1) * limit; // 레코드 시작 위치 번호

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
                .orderBy(new OrderSpecifier(Order.DESC, pathBuilder.get("createdAt")))
                .fetch();


        /* 검색 조건 처리 S */
//        String email = search.getEmail();
//        String userName = search.getUserName();
//
//        String sopt = search.getSopt();
//        sopt = StringUtils.hasText(sopt) ? sopt.trim() : "ALL";
//        String skey = search.getSkey();
//
//        if(StringUtils.hasText(email)){
//            andBuilder.and(member.email.contains(email.trim()));
//        }
//
//        if(StringUtils.hasText(userName)){
//            andBuilder.and(member.userName.contains(userName.trim()));
//        }
//
//        //조건별 키워드 검색
//        if(StringUtils.hasText(skey)){
//            skey = skey.trim();
//
//            BooleanExpression cond1 = member.email.contains(skey);
//            BooleanExpression cond2 = member.userName.contains(skey);
//
//            if(sopt.equals("email")){
//                andBuilder.and(cond1);
//            } else if (sopt.equals("userName")) {
//                andBuilder.and(cond2);
//            }else {
//                BooleanBuilder orBuilder = new BooleanBuilder();
//                orBuilder.or(cond1)
//                        .or(cond2);
//                andBuilder.and(orBuilder);
//            }
//        }
//
//        /* 검색 조건 처리 E */

        /* 페이징 처리 S */
        int total = (int)memberRepository.count(andBuilder); // 총 레코드 갯수

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        Page<Member> data = memberRepository.findAll(andBuilder,pageable);

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), limit, 10, request);
        /* 페이징 처리 E */

        return new ListData<>(items, pagination);
        //        return new ListData<>(data.getContent(), pagination);
    }

    /**
     * 회원검색
     */
    public List<Member> searchMembers(MemberSearch search) {
        BooleanBuilder builder = new BooleanBuilder();
        QMember member = QMember.member;

        String skey = search.getSkey();  // 검색 키워드
        String sopt = search.getSopt();  // 검색 옵션

        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            // 검색 옵션에 따른 검색 조건 설정
            if ("email".equals(sopt)) {
                builder.and(member.email.contains(skey));
            } else if ("userName".equals(sopt)) {
                builder.and(member.userName.contains(skey));
            } else if ("ALL".equals(sopt)) {
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(member.email.contains(skey))
                        .or(member.userName.contains(skey));
                builder.and(orBuilder);
            }
        }

        // 쿼리 실행
        return new JPAQueryFactory(em)
                .selectFrom(member)
                .leftJoin(member.authorities)
                .fetchJoin()
                .where(builder)
                .fetch();
    }

    public RequestMember getForm(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        RequestMember form = new ModelMapper().map(member, RequestMember.class);

        List<String>  authorities = member.getAuthorities().stream().map(a -> a.getAuthority().name()).toList();
        form.setAuthorities(authorities);
        form.setMode("edit");
        form.setActivity(true);
        return form;
    }
}
