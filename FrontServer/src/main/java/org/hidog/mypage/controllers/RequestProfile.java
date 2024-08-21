package org.hidog.mypage.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class RequestProfile {

    @NotBlank(message = "닉네임을 입력하세요.")
    private String userName;

    @Email(message = "이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력하세요.")
    private String password;

    @NotBlank(message = "주소를 입력하세요.")
    private String address;
}