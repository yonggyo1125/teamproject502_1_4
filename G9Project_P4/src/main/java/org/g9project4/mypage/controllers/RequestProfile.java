package org.g9project4.mypage.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestProfile {
    @NotBlank
    private String userName;

    private String password;

    private String confirmPassword;

    private String mobile;
}
