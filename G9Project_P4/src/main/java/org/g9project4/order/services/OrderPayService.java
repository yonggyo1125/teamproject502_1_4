package org.g9project4.order.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.order.constants.OrderStatus;
import org.g9project4.order.entities.OrderInfo;
import org.g9project4.order.entities.OrderItem;
import org.g9project4.payment.constants.PayMethod;
import org.g9project4.payment.controllers.PayConfirmResult;
import org.g9project4.payment.services.PaymentConfig;
import org.g9project4.payment.services.PaymentConfigService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPayService {
    private final OrderInfoService orderInfoService;
    private final OrderStatusService orderStatusService;
    private final PaymentConfigService paymentConfigService;

    public PaymentConfig getConfig(Long orderNo) {
        OrderInfo orderInfo = orderInfoService.get(orderNo);
        PaymentConfig config = paymentConfigService.get(orderNo, orderInfo.getTotalPayPrice());

        OrderItem orderItem = orderInfo.getOrderItem();
        String goodName = orderItem.getItemName();
        int qty = orderItem.getQty();
        if (qty > 1) goodName += " X " + qty + "개";

        config.setOid(orderNo);
        config.setPrice(orderInfo.getTotalPayPrice());
        config.setGoodname(goodName);
        config.setBuyertel(orderInfo.getOrderMobile());
        config.setBuyeremail(orderInfo.getOrderEmail());

        return config;
    }

    public void update(PayConfirmResult result) {
        Long orderNo = result.getOrderNo();
        PayMethod method = result.getPayMethod();
        OrderStatus status = method.equals(PayMethod.VBank) ? OrderStatus.APPLY : OrderStatus.INCASH;

        // 주문서에 결제 정보 업데이트
        OrderInfo orderInfo = orderInfoService.get(orderNo);
        orderInfo.setPayLog(result.getPayLog());
        orderInfo.setPayTid(result.getTid());

        // 주문 상태 변경
        orderStatusService.change(orderNo, status);
    }
}
