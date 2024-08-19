package org.g9project4.order.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.member.entities.Member;
import org.g9project4.payment.constants.PayMethod;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class OrderInfo extends BaseEntity {
    @Id
    private Long orderNo = System.currentTimeMillis();

    @OneToMany(mappedBy = "orderInfo", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length=40, nullable = false)
    private String orderName; // 주문자명

    @Column(length=100, nullable = false)
    private String orderEmail; // 주문자 이메일

    @Column(length=20, nullable = false)
    private String orderMobile; // 주문자 휴대전화번호

    @Column(length=40, nullable = false)
    private String receiverName; //받는분 이름

    @Column(length=20, nullable = false)
    private String receiverMobile; // 받는분 휴대전화번호

    @Column(length=20, nullable = false)
    private String zoneCode; // 우편번호

    @Column(length=100, nullable = false)
    private String address; // 배송지 주소

    private String addressSub; // 나머지 배송지 주소

    private String deliveryMemo; // 배송 메모

    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Lob
    private String payLog; // 결제 로그

    @Column(length=65)
    private String payTid; // PG 거래 ID(tid)
}