package org.hidog.order.services;

import lombok.RequiredArgsConstructor;
import org.hidog.order.constants.OrderStatus;
import org.hidog.order.entities.OrderInfo;
import org.hidog.order.entities.OrderItem;
import org.hidog.order.repository.OrderInfoRepository;
import org.hidog.payment.constants.PayMethod;
import org.hidog.payment.controllers.PayConfirmResult;
import org.hidog.payment.services.PaymentConfig;
import org.hidog.payment.services.PaymentConfigService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPayService {
    private final OrderInfoService orderInfoService;
    private final OrderStatusService orderStatusService;
    private final PaymentConfigService paymentConfigService;
    private final OrderInfoRepository orderInfoRepository;

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

    public OrderInfo update(PayConfirmResult result) {
        Long orderNo = result.getOrderNo();
        PayMethod method = result.getPayMethod();
        OrderStatus status = method.equals(PayMethod.VBANK) ? OrderStatus.APPLY : OrderStatus.INCASH;

        // 주문서에 결제 정보 업데이트
        OrderInfo orderInfo = orderInfoService.get(orderNo);
        orderInfo.setPayLog(result.getPayLog());
        orderInfo.setPayTid(result.getTid());
        orderInfo.setPayBankName(result.getBankName());
        orderInfo.setPayBankAccount(result.getBankAccount());
        orderInfoRepository.saveAndFlush(orderInfo);

        // 주문 상태 변경
        orderStatusService.change(orderNo, status);

        return orderInfo;
    }
}
