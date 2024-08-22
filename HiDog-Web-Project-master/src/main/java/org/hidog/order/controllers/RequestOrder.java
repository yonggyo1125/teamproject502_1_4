package org.hidog.order.controllers;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hidog.payment.constants.PayMethod;

@Data
public class RequestOrder {

    @NotNull
    private Long bSeq = 952L; //게시글 번호

    private long OrderNo = System.currentTimeMillis(); //주문번호


    @NotBlank
    private String orderName; //주문자 이름

    @NotBlank @Email
    private String orderEmail; //주문자 이메일

    @NotBlank
    private String orderMobile; //주문자 휴대전화번호

    @NotBlank
    private String receiverName; //받는사람 이름

    @NotBlank
    private String receiverMobile; //받는사람 휴대전화번호

    @NotBlank
    private String zoneCode; //우편변호

    @NotBlank
    private String address; //배송지 주소

    private String addressSub; //나머지 배송지 주소

    private String deliveryMemo; //배송 매모

    private String payMethod = PayMethod.CARD.name(); //결제수단

    @AssertTrue
    private boolean agree; //약관동의

}
