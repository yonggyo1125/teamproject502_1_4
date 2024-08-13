package org.g9project4.order.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.config.services.ConfigInfoService;
import org.g9project4.global.SHA256;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentConfigService {
    private final ConfigInfoService infoService;

    public PaymentConfig get(long oid, int price) {
        PaymentConfig config = infoService.get("payment", PaymentConfig.class)
                .orElseGet(PaymentConfig::new);
        long timestamp = new Date().getTime();
        String signKey = config.getSignKey();

        try {
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

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        config.setTimestamp(timestamp);
        return config;
    }


}
