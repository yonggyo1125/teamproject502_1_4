package org.hidog.order.services;

import lombok.RequiredArgsConstructor;
import org.hidog.order.entities.OrderInfo;
import org.hidog.order.entities.OrderItem;
import org.hidog.order.exceptions.OrderNotFoundException;
import org.hidog.order.repository.OrderInfoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderInfoService {

    private final OrderInfoRepository orderInfoRepository;

    public OrderInfo get(Long orderNo){
        OrderInfo orderInfo = orderInfoRepository.findById(orderNo).orElseThrow(OrderNotFoundException::new);
        addInfo(orderInfo);
        return orderInfo;
    }

    //추가처리 (총금액)
    public void addInfo(OrderInfo orderInfo){
        OrderItem orderItem = orderInfo.getOrderItem();
        int totalPayPrice = orderItem.getPrice() * orderItem.getQty();
        orderInfo.setTotalPayPrice(totalPayPrice);
    }
}
