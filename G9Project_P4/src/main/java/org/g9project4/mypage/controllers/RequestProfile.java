package org.g9project4.mypage.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestProfile {
    //@NotBlank(message = "회원명을 입력하세요.")
    private String userName;

    //@NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    private String confirmPassword;
}
