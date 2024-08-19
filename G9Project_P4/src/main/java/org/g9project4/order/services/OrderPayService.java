package org.g9project4.order.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.payment.services.PaymentConfig;
import org.g9project4.payment.services.PaymentConfigService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPayService {

    private final PaymentConfigService paymentConfigService;

    public PaymentConfig getConfig(Long orderNo) {

        return null;
    }
}
