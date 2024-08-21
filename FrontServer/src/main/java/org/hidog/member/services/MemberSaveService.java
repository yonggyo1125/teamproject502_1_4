package org.hidog.member.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hidog.member.MemberUtil;
import org.hidog.member.constants.Authority;
import org.hidog.member.controllers.RequestJoin;
import org.hidog.member.entities.Authorities;
import org.hidog.member.entities.Member;
import org.hidog.member.exceptions.MemberNotFoundException;
import org.hidog.member.repositories.AuthoritiesRepository;
import org.hidog.member.repositories.MemberRepository;
import org.hidog.mypage.controllers.RequestProfile;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberUtil memberUtil;
    private final HttpSession session;

    /**
     * 회원 가입 처리
     * @param form 회원 가입 폼
     */
    public void save(RequestJoin form) {
        Member member = new ModelMapper().map(form, Member.class);

        // 닉네임 중복 체크
        if (memberRepository.existsByUserName(member.getUserName())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");  // 중복 시 예외 발생
        }

        String hash = passwordEncoder.encode(member.getPassword());
        member.setPassword(hash);
        save(member, List.of(Authority.USER));
    }

    /**
     * 회원정보 수정
     * @param form 회원 정보 수정 폼
     */
    public void save(RequestProfile form) {
        Member member = memberUtil.getMember();
        String email = member.getEmail();
        member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        String userName = form.getUserName();
        String password = form.getPassword();

        // 닉네임 중복 체크
        if (memberRepository.existsByUserName(userName) && !member.getUserName().equals(userName)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");  // 중복 시 예외 발생
        }

        member.setUserName(userName);

        if (StringUtils.hasText(password)) {
            String hash = passwordEncoder.encode(password);
            member.setPassword(hash);
        }

        memberRepository.saveAndFlush(member);

        session.setAttribute("userInfoChanged", true);
    }

    /**
     * 회원 및 권한 저장
     * @param member       회원
     * @param authorities  권한 리스트
     */
    public void save(Member member, List<Authority> authorities) {
        memberRepository.saveAndFlush(member);
        if (authorities != null) {
            List<Authorities> items = authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();

            items = authorities.stream()
                    .map(authority -> Authorities.builder().member(member).authority(authority).build()).toList();
            authoritiesRepository.saveAllAndFlush(items);
        }
    }
}