package org.g9project4.payment.controllers;

import lombok.Builder;
import lombok.Data;
import org.g9project4.payment.constants.PayMethod;

@Data
@Builder
public class PayConfirmResult {
    private Long orderNo;
    private String payLog;
    private String tid;
    private PayMethod payMethod;
    private String bankName; // 가상계좌 입금은행
    private String bankAccount; // 가상계좌번호
    private String cancelUrl; // 취소 URL
}
