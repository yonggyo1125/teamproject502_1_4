package org.hidog.order.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hidog.global.entities.BaseEntity;
import org.hidog.member.entities.Member;
import org.hidog.order.constants.OrderStatus;
import org.hidog.payment.constants.PayMethod;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo extends BaseEntity {

    @Id
    private Long orderNo = System.currentTimeMillis();

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.START;

    @OneToOne(mappedBy ="orderInfo", fetch = FetchType.LAZY)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false, length = 40)
    private String orderName; //주문자 이름
    @Column(nullable = false, length = 100)
    private String orderEmail; //주문자 이메일
    @Column(nullable = false, length = 20)
    private String orderMobile; //주문자 휴대전화번호

    @Column(nullable = false, length = 40)
    private String receiverName; //받는사람 이름
    @Column(nullable = false, length = 20)
    private String receiverMobile; //받는사람 휴대전화번호

    @Column(nullable = false, length = 20)
    private String zoneCode; //우편변호

    @Column(nullable = false, length = 40)
    private String address; //배송지 주소

    private String addressSub; //나머지 배송지 주소

    private String deliveryMemo; //나머지 배송지 주소

    @Column(length = 60)
    private String deliveryCompany; //배송업체
    @Column(length = 60)
    private String deliveryInvoice; //운송장번호

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Lob
    private String payLog; //결제 로그

    @Column(length = 65)
    private String payTid; //PG 거래 ID(tid)

    @Column(length = 40)
    private String payBankName; //가상 계좌 은행
    @Column(length = 40)
    private String payBankAccount; //가상 계좌



    @Transient
    private int totalPayPrice; //총결제금액

}
