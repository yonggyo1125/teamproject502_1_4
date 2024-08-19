package org.g9project4.order.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.order.entities.OrderInfo;
import org.g9project4.order.entities.OrderItem;
import org.g9project4.order.exceptions.OrderNotFoundException;
import org.g9project4.order.repositories.OrderInfoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderInfoService {
    private final OrderInfoRepository orderInfoRepository;

    public OrderInfo get(Long orderNo) {
        OrderInfo orderInfo = orderInfoRepository.findById(orderNo).orElseThrow(OrderNotFoundException::new);

        addInfo(orderInfo);

        return orderInfo;
    }

    // 추가 처리
    public void addInfo(OrderInfo orderInfo) {
        OrderItem orderItem = orderInfo.getOrderItem();
        int totalPayPrice = orderItem.getPrice() * orderItem.getQty();

        orderInfo.setTotalPayPrice(totalPayPrice);

    }
}
