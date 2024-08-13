package org.g9project4.order.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.config.services.ConfigInfoService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConfigService {
    private final ConfigInfoService infoService;

    public PaymentConfig get() {
        PaymentConfig config = infoService.get("payment", PaymentConfig.class)
                .orElseGet(PaymentConfig::new);

        return config;
    }
}
