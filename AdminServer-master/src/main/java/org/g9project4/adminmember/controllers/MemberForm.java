package org.g9project4.adminmember.controllers;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberForm {
    private String mode = "edit";
    private boolean activity;

    private Long seq;
    private String gid;
    private String userName;
    private String email;
    private String tel;
    private List<String> authorities = new ArrayList<>();
}
