package org.g9project4.order.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.board.entities.BoardData;
import org.g9project4.board.services.BoardInfoService;
import org.g9project4.member.MemberUtil;
import org.g9project4.order.constants.OrderStatus;
import org.g9project4.order.controllers.RequestOrder;
import org.g9project4.order.entities.OrderInfo;
import org.g9project4.order.entities.OrderItem;
import org.g9project4.order.repositories.OrderInfoRepository;
import org.g9project4.order.repositories.OrderItemRepository;
import org.g9project4.payment.constants.PayMethod;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderSaveService {
    private final OrderInfoRepository infoRepository;
    private final OrderItemRepository itemRepository;
    private final BoardInfoService boardInfoService;
    private final MemberUtil memberUtil;

    public OrderInfo save(RequestOrder form) {
        // 게시글 조회
        BoardData boardData = boardInfoService.get(form.getBSeq());

        long orderNo = form.getOrderNo() < 1L ? System.currentTimeMillis() : form.getOrderNo();

        Long num1 = boardData.getNum1();
        Long num2 = boardData.getNum2();

        int price = num1 == null ? 0 : num1.intValue();
        int qty = num2 == null && num2 < 1L ? 1 : num2.intValue();
        String itemName = boardData.getText1();

        /* 주문서 정보 저장 S */
        OrderInfo orderInfo = new ModelMapper().map(form, OrderInfo.class);
        orderInfo.setPayMethod(PayMethod.valueOf(form.getPayMethod()));
        orderInfo.setOrderNo(orderNo);
        orderInfo.setMember(memberUtil.getMember());
        orderInfo.setStatus(OrderStatus.START);

        String orderMobile = form.getOrderMobile();
        if (StringUtils.hasText(orderMobile)) {
            orderMobile = orderMobile.replaceAll("\\D", "");
            orderInfo.setOrderMobile(orderMobile);
        }

        infoRepository.saveAndFlush(orderInfo);
        /* 주문서 정보 저장 E */

        /* 주문 상품 정보 저장 S */
        OrderItem orderItem = OrderItem.builder()
                .orderInfo(orderInfo)
                .itemName(itemName)
                .price(price)
                .qty(qty)
                .boardData(boardData)
                .build();
        itemRepository.saveAndFlush(orderItem);
        /* 주문 상품 정보 저장 E */

        return orderInfo;
    }
}
