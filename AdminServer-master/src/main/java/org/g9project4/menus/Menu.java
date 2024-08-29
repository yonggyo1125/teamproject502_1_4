package org.g9project4.menus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private final static Map<String, List<MenuDetail>> menus;

    static {

        menus = new HashMap<>();

        menus.put("config", Arrays.asList(
                new MenuDetail("basic", "기본설정", "/config"),
                new MenuDetail("api", "API 설정", "/config/api"),
                new MenuDetail("update","API 업데이트", "/config/api/update")
        ));

        menus.put("member", Arrays.asList(
            new MenuDetail("list", "회원목록", "/manager/member"),
            new MenuDetail("authority", "회원권한", "/member/authority")
        ));
        
        menus.put("board", Arrays.asList(
                new MenuDetail("list", "게시판목록", "/board"),
                new MenuDetail("add", "게시판등록", "/board/add"),
                new MenuDetail("posts", "게시글관리", "/board/posts")
        ));
    }

    public static List<MenuDetail> getMenus(String code) {
        return menus.get(code);
    }
}
