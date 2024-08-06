package org.g9project4.mypage.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.entities.Member;
import org.g9project4.member.repositories.MemberRepository;
import org.g9project4.mypage.controllers.RequestProfile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class MyPageService {

    // private final MyPageUpdateValidator validator;
    private final MemberUtil memberUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    public void update(RequestProfile form) {

        Member member = memberUtil.getMember();


        BindingResult bindingResult = new BeanPropertyBindingResult(form, "form");
        //validator.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {

            //throw new ValidationException("유효성 검증 오류가 발생했습니다.");
        }


        String userName = form.getUserName();
        String password = form.getPassword();

        member.setUserName(userName);
        if (password != null && !password.isBlank()) {
            String hash = encoder.encode(password);
            member.setPassword(hash);
        }


        memberRepository.save(member);
    }
}
