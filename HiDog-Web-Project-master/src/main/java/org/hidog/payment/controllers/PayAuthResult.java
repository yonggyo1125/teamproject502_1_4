package org.hidog.payment.controllers;

import lombok.Data;

@Data
public class PayAuthResult {
    private String resultCode;
    private String resultMsg;
    private String mid;
    private String orderNumber;
    private String authToken; // 승인요청 검증 토큰
    private String idc_name;
    private String authUrl; // 승인 요청 url
    private String netCancelUrl; // 망 취소 요청 url
    private String charset;
    private String merchantData; // 가맹점 임의 데이터
}
