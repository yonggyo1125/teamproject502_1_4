package org.g9project4.order.controllers;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RequestOrder {

    @NotNull
    private Long bSeq = 952L; // 게시글 번호

    @NotBlank
    private String orderName; // 주문자명

    @NotBlank
    private String orderEmail; // 주문자 이메일

    @NotBlank
    private String orderMobile; // 주문자 휴대전화번호

    @NotBlank
    private String receiverName; //받는분 이름

    @NotBlank
    private String receiverMobile; // 받는분 휴대전화번호

    @NotBlank
    private String zoneCode; // 우편번호

    @NotBlank
    private String address; // 배송지 주소

    private String addressSub; // 나머지 배송지 주소

    private String deliveryMemo; // 배송 메모

    private String payMethod; // 결재 수단
}
