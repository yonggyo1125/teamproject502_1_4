package org.hidog.global.advices;

import lombok.RequiredArgsConstructor;
import org.hidog.member.MemberUtil;
import org.hidog.member.entities.Member;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice("org.hidog")// 범위 설정
@RequiredArgsConstructor
public class CommonControllerAdvice {//전역에서 확인 가능

    private final MemberUtil memberUtil;

    @ModelAttribute("loggedMember")
    public Member loggedMember() {
        return memberUtil.getMember();
    }

    @ModelAttribute("isLogin")
    public boolean isLogin() {
        return memberUtil.isLogin();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        return memberUtil.isAdmin();
    }
}