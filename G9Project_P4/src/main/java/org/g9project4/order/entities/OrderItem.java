package org.g9project4.order.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.board.entities.BoardData;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.order.constants.OrderStatus;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;

    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.START;
    
    private String itemName; // 주문 상품명

    @ManyToOne(fetch = FetchType.LAZY)
    private BoardData boardData;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderInfo orderInfo;

    private int price; // 상품 단품 금액

    private int qty; // 주문 수량
}
