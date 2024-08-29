package org.g9project4.adminmember.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.g9project4.member.constants.Authority;
import org.g9project4.member.entities.Authorities;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestMember {
    public Long seq;
    public String mode = "edit";

    private boolean activity;
    @NotBlank
    private String email;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    private String mobile;
    private String type = "ALL";

    private List<String> authorities = new ArrayList<>();

}
