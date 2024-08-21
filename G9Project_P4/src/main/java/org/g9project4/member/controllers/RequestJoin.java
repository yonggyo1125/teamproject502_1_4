package org.g9project4.member.controllers;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class RequestJoin implements Serializable {

    private final long serialVersionUID = 1L;

    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 8)
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    private String userName;
    @NotBlank
    private String mobile;
    @AssertTrue
    private boolean agree;

    private String gid = UUID.randomUUID().toString();
}
