package org.g9project4.member.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.exceptions.RestExceptionProcessor;
import org.g9project4.global.rests.JSONData;
import org.g9project4.member.repositories.MemberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class ApiMemberController implements RestExceptionProcessor {

    private final MemberRepository memberRepository;

    /**
     * 이메일 중복 여부 체크
     * @param email
     * @return
     */
    @GetMapping("/email_dup_check")
    public JSONData duplicateEmailCheck(@RequestParam("email") String email) {
        boolean isExists = memberRepository.exists(email);

        JSONData data = new JSONData();
        data.setSuccess(isExists);

        return data;
    }
}