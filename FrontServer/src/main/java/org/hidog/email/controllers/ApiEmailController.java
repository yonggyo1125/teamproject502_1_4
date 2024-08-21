package org.hidog.email.controllers;

import lombok.RequiredArgsConstructor;
import org.hidog.email.services.EmailVerifyService;
import org.hidog.global.rests.JSONData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class ApiEmailController {

    private final EmailVerifyService verifyService;

    /**
     * 이메일 인증 코드 발급
     *
     * @param email
     * @return
     */
    @GetMapping("/verify")
    public JSONData<Object> sendVerifyEmail(@RequestParam("email") String email) {
        JSONData<Object> data = new JSONData<>();

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
    public JSONData<Object> checkVerifiedEmail(@RequestParam("authNum") int authNum) {
        JSONData<Object> data = new JSONData<>();

        boolean result = verifyService.check(authNum);
        data.setSuccess(result);

        return data;
    }
}