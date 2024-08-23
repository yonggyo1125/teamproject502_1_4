package org.hidog.payment.services;

import lombok.RequiredArgsConstructor;
import org.hidog.global.SHA512;
import org.hidog.order.entities.OrderInfo;
import org.hidog.order.services.OrderInfoService;
import org.hidog.payment.constants.PayMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class PaymentCancelService {
    private final OrderInfoService orderInfoService;
    private final PaymentConfigService configService;
    private final RestTemplate restTemplate;

    /**
     *
     * @param orderNo : 주문번호
     * @param message : 취소 메세지
     */
    public void cancel(Long orderNo, String message) {
        OrderInfo orderInfo = orderInfoService.get(orderNo);
        PaymentConfig config = configService.get();

        PayMethod payMethod = orderInfo.getPayMethod();
        String tid = orderInfo.getPayTid();

        String iniApiKey = config.getIniApiKey();
        String iniApiIv = config.getIniApiIv();
        String mid = config.getMid();
        long timestamp = config.getTimestamp();
        String ip = "127.0.0.1";

        String text = iniApiKey + "Refund" + payMethod.name() + timestamp + ip + mid + tid;

        try {
           String hashData = SHA512.encrypt(text);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }
}
