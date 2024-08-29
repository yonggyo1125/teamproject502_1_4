package org.g9project4.adminmember.constants;

import java.util.List;

public enum Authority {
    ALL("전체"),
    USER("일반회원"),
    ADMIN("관리자");

    private final String title;

    Authority(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static List<String[]> getList() {
        return List.of(
                new String[] {ALL.name(), ALL.title},
                new String[] {USER.name(), USER.title},
                new String[] {ADMIN.name(), ADMIN.title}
        );
    }
    public static List<Authority> getAuthorities() {
        return List.of(Authority.ALL, Authority.USER, Authority.ADMIN);
    }
}

