package org.hidog.order.constants;

import java.util.List;

public enum OrderStatus {

    START("접수중"), //주문서 작성 시작
    APPLY("주문접수"), //주문서 접수
    INCASH("입금확인"), //입금 확인
    PREPARE("상품_준비중"), //상품 준비중
    DELIVERY("배송중"), //배송중
    COMPLETE("주문완료"), //주문완료
    CANCEL("주문취소"), //주문취소(입금전)
    REFUND("환불"), //환불(입금완료, 배송전, 배송완료)
    RETURN("반품"); //반품(배송 완료 후)

    private final String title;
    OrderStatus(String title){
        this.title = title;
    }

    public static List<String[]> getList(){
        return List.of(
                new String[] {START.name(), START.title},
                new String[] {APPLY.name(), APPLY.title},
                new String[] {INCASH.name(), INCASH.title},
                new String[] {PREPARE.name(), PREPARE.title},
                new String[] {DELIVERY.name(), DELIVERY.title},
                new String[] {COMPLETE.name(), COMPLETE.title},
                new String[] {CANCEL.name(), CANCEL.title},
                new String[] {REFUND.name(), REFUND.title},
                new String[] {RETURN.name(), RETURN.title}
        );
    }
}
