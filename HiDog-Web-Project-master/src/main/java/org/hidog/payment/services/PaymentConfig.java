package org.hidog.payment.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentConfig {
    private String mid; //상점 아이디 (이니시스)
    private String signKey; // 사인키 // 데이터 위변조 방지용
    private List<String> payMethods; //결제 수단
    private Long timestamp;
    private String signature; // oid, price, timestamp
    private String verification; // oid, price, timestamp, signKey 를 가지고 만드는 해시코드
    private String mKey; // mid, signKey 를 가지고 만드는 해시코드

    private Long oid;
    private int price;
    private String goodname;
    private String buyername;
    private String buyertel;
    private String buyeremail;
}
