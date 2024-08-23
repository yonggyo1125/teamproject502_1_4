package org.hidog.config.controllers;

import lombok.Data;

import java.util.List;

@Data
public class PaymentConfig {

    private String mid; //상점 아이디 (이니시스)
    private String signKey; // 사인키
    private String iniApiKey; // 취소시 필요
    private String iniApiIv; // 취소시 필요
    private List<String> payMethods; //결제 수단
}
