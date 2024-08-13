package org.g9project4.order.constants;

import java.util.List;

public enum PayMethod {
    CARD("신용카드"),
    DirectBank("실시간계좌이체"),
    VBank("가상계좌");

    private final String title;

    PayMethod(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static List<String[]> getList() {
       return List.of(
               new String[] {CARD.name(), CARD.title},
               new String[] {DirectBank.name(), DirectBank.title},
               new String[] { VBank.name(), VBank.title}
        );
    }
}
