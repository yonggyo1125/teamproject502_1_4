package org.hidog.payment.services;

import lombok.RequiredArgsConstructor;
import org.hidog.config.services.ConfigInfoService;
import org.hidog.global.SHA256;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentConfigService {
    private final ConfigInfoService infoService;

    public PaymentConfig get(Long oid, Integer price) {
        try {
            PaymentConfig config = infoService.get("payment", PaymentConfig.class)
                    .orElseGet(PaymentConfig::new);

            if (oid == null || price == null) {
                return config;
            }

            long timestamp = new Date().getTime();
            String signKey = config.getSignKey();


            // signature S
            String data = String.format("oid=%d&price=%d&timestamp=%d", oid, price, timestamp);
            String signature = SHA256.encrypt(data);
            config.setSignature(signature);
            // signature E

            // verification S
            data = String.format("oid=%d&price=%d&signKey=%s&timestamp=%d", oid, price, signKey, timestamp);
            String verification = SHA256.encrypt(data);
            config.setVerification(verification);
            // verification E

            // mKey
            String mKey = SHA256.encrypt(signKey);
            config.setMKey(mKey);

            config.setTimestamp(timestamp);
            return config;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public PaymentConfig get() {
        return get(null, null);
    }
}
