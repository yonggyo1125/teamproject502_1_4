package org.hidog.order.services;


import lombok.RequiredArgsConstructor;
import org.hidog.order.constants.OrderStatus;
import org.hidog.order.entities.OrderInfo;
import org.hidog.order.repository.OrderInfoRepository;
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
        orderInfo.setDeliveryCompany(extra.get("deliveryCompnay"));
        orderInfo.setDeliveryInvoice(extra.get("deliveryInvoice"));

        infoRepository.saveAndFlush(orderInfo);

        //단계별 이메일 전송(앞으로 추가)
    }

    public void change(Long orderNo, OrderStatus status) {
        change(orderNo, status, null);
    }
}
