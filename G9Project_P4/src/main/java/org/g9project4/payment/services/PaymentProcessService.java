package org.g9project4.payment.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.SHA256;
import org.g9project4.global.Utils;
import org.g9project4.payment.controllers.PayAuthResult;
import org.g9project4.payment.exceptions.PaymentAuthException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentProcessService {
    private final PaymentConfigService configService;
    private final RestTemplate restTemplate;
    private final Utils utils;

    /**
     * 인증 데이터로 결제 승인 처리
     * @param result
     */
    public void process(PayAuthResult result) {

        String mid = result.getMid();
        String authToken = result.getAuthToken();
        String authUrl = result.getAuthUrl();
        String netCancelUrl = result.getNetCancelUrl();

        PaymentConfig config = configService.get();
        String signKey = config.getSignKey();

        long timestamp = new Date().getTime();

        // 인증 실패, 승인 실패시 이동할 주소
        String orderNumber = result.getOrderNumber();
        String redirectUrl = utils.redirectUrl("/order/order_fail?orderNo=" + orderNumber);

        if (!result.getResultCode().equals("0000")) { // 인증 실패시

            // 결제 승인 취초

            throw new PaymentAuthException(result.getResultMsg(), redirectUrl);
        }



        // 요청 데이터

        String signature = "";
        String verification = "";
        try {
            signature = SHA256.encrypt(String.format("authToken=%s&timestamp=%d", authToken, timestamp));
            verification = SHA256.encrypt(String.format("authToken=%s&signKey=%s&timestamp=%d", authToken, signKey, timestamp));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("mid", mid);
        params.add("authToken", authToken);
        params.add("timestamp", String.valueOf(timestamp));
        params.add("signature", signature);
        params.add("verification", verification);
        params.add("charset", "UTF-8");
        params.add("format", "JSON");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(URI.create(authUrl), request, String.class);

        System.out.println(response);
    }
}
