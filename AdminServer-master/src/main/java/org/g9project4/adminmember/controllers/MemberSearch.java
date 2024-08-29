package org.g9project4.adminmember.controllers;

import lombok.Data;

@Data
public class MemberSearch {
    private int page = 1;
    private int limit = 20;
    private String userName;
    private String email;
    private String sopt;
    private String skey;
}