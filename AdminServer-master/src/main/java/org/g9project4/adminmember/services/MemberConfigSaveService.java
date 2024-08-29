package org.g9project4.adminmember.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.adminmember.controllers.RequestMember;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.script.AlertException;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.entities.Authorities;
import org.g9project4.member.entities.AuthoritiesId;
import org.g9project4.member.entities.Member;
import org.g9project4.member.repositories.AuthoritiesRepository;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.member.services.MemberSaveService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberConfigSaveService {

    private final MemberRepository memberRepository;
    private final Utils utils;
    private final AuthoritiesRepository authoritiesRepository;


    public void save(RequestMember form){
        Member member = memberRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(form.getEmail()));

        member.setUserName(form.getUserName());
        member.setEmail(form.getEmail());
        member.setMobile(form.getMobile());

        member.getAuthorities().clear(); //기존 권한 지우고 새로 설정
        List<String> strAuthorities = form.getAuthorities();

        List<Authorities> authoList = new ArrayList<>();
        List<Authorities> authorities = authoritiesRepository.findByMember(member);
        //기존 authorities 데이터 삭제
        for(Authorities autho : authorities){
            authoritiesRepository.deleteById(new AuthoritiesId(autho.getMember(),autho.getAuthority()));
        }
        Authorities _autho = new Authorities();
        for(String autho : strAuthorities){
            _autho.setMember(member);
            _autho.setAuthority(Authority.valueOf(autho));

            authoList.add(_autho);
        }
        member.setAuthorities(authoList);// 새로 생성한 authoList를 member에 설정
        System.out.println(member);
        System.out.println(member.getAuthorities());
        memberRepository.saveAndFlush(member);
    }

    public void saveList(List<Integer> chks) {
        if (chks == null || chks.isEmpty()) {
            throw new AlertException("수정할 회원을 선택하세요.", HttpStatus.BAD_REQUEST);
        }

        for (int chk : chks) {
            String userEmail = utils.getParam("email_" + chk);

            Member member = memberRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new AlertException("해당 이메일의 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

            if (member == null) continue;
            System.out.println("Before save: " + member.getAuthorities());

            String strAuthority = utils.getParam("authority_" + chk);

            if (strAuthority == null || strAuthority.isEmpty()) {
                throw new AlertException("권한 값이 제공되지 않았습니다.", HttpStatus.BAD_REQUEST);
            }

            try {
                Authority authority = Authority.valueOf(strAuthority);
                Authorities authorities = new Authorities();
                authorities.setMember(member);
                authorities.setAuthority(authority);

                member.getAuthorities().clear();
                member.getAuthorities().add(authorities);

                memberRepository.saveAndFlush(member);
                System.out.println("After save: " + memberRepository.findByEmail(member.getEmail()).get().getAuthorities());
            } catch (IllegalArgumentException e) {
                throw new AlertException("유효하지 않은 권한 값입니다.", HttpStatus.BAD_REQUEST);
            }
        }

        // 모든 수정된 사항을 데이터베이스에 반영
        memberRepository.flush();
    }
}