package org.hidog.order.constants;

import java.util.List;

public enum PayMethod {
    CARD("신용카드"), //카드
    DIRECTBANK("실시간계좌이체"), //실시간 계좌이체
    VBANK("가상계좌"); //가상계좌

    private final String title;
    PayMethod(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public static List<String[]> getList(){
        return List.of(
                new String[] {CARD.name(), CARD.title},
                new String[]{DIRECTBANK.name(), DIRECTBANK.title},
                new String[]{VBANK.name(), VBANK.title}
        );
    }
}
