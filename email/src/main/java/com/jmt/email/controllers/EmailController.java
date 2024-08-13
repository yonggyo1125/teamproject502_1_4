package com.jmt.email.controllers;

import com.jmt.email.services.EmailVerifyService;
import com.jmt.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class EmailController {
    private final EmailVerifyService verifyService;

    /**
     * 이메일 인증 코드 발급
     *
     * @param email
     * @return
     */
    @GetMapping("/verify")
    public JSONData sendVerifyEmail(@RequestParam("email") String email) {
        JSONData data = new JSONData();

        boolean result = verifyService.sendCode(email);
        data.setSuccess(result);

        return data;
    }

    /**
     * 발급받은 인증코드와 사용자 입력 코드의 일치 여부 체크
     *
     * @param authNum
     * @return
     */
    @GetMapping("/auth_check")
    public JSONData checkVerifiedEmail(@RequestParam("authNum") int authNum) {
        JSONData data = new JSONData();

        boolean result = verifyService.check(authNum);
        data.setSuccess(result);

        return data;
    }
}
