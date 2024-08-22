package org.hidog.mypage.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestProfile {

    @NotBlank @Size(min = 2, max = 10)
    private String userName;

    private String password;

    private String confirmPassword;

    @NotBlank
    private String address;

    private String detailAddress;
}