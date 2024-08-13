package org.g9project4.order.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentConfig {
    private String mid; // 상점 아이디
    private String signKey; // 사인키
    private List<String> payMethods; // 결제 수단
    private Long timestamp;
    private String signature; // oid, price, timestamp
    private String verification; // oid, price, signKey, timestamp
    private String mKey; // mid, signKey
}
