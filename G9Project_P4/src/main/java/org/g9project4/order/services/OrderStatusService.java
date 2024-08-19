package org.g9project4.order.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.order.constants.OrderStatus;
import org.g9project4.order.entities.OrderInfo;
import org.g9project4.order.repositories.OrderInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderInfoService infoService;
    private final OrderInfoRepository infoRepository;

    public void change(Long orderNo, OrderStatus status, Map<String, String> extra) {
        OrderInfo orderInfo = infoService.get(orderNo);
        orderInfo.setStatus(status);
        orderInfo.setDeliveryCompany(extra.get("deliveryCompany"));
        orderInfo.setDeliveryInvoice(extra.get("deliveryInvoice"));

        infoRepository.saveAndFlush(orderInfo);

        // 단계별 이메일 전송
    }

    public void change(Long orderNo, OrderStatus status) {
        change(orderNo, status, null);
    }
}
