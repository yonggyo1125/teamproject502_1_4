package org.g9project4.payment.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.Utils;
import org.g9project4.payment.controllers.PayAuthResult;
import org.g9project4.payment.exceptions.PaymentAuthException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

        // 인증 실패, 승인 실패시 이동할 주소
        String orderNumber = result.getOrderNumber();
        String redirectUrl = utils.redirectUrl("/order/order_fail?orderNo=" + orderNumber);

        if (!result.getResultCode().equals("0000")) { // 인증 실패시

            // 결제 승인 취초

            throw new PaymentAuthException(result.getResultMsg(), redirectUrl);
        }

        // 요청 데이터
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();


    }
}
