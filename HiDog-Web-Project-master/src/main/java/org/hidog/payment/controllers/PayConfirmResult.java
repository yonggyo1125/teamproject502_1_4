package org.hidog.payment.controllers;

import lombok.Builder;
import lombok.Data;
import org.hidog.payment.constants.PayMethod;

@Data
@Builder
public class PayConfirmResult {

    private Long orderNo;
    private String payLog;
    private String tid;
    private PayMethod payMethod;
    private String bankName; //가상계좌 입금은행
    private String bankAccount; //가상계좌번호
    private String cancelUrl; //취소 URL

}
