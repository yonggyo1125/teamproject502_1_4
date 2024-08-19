package org.g9project4.order.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderInfo is a Querydsl query type for OrderInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderInfo extends EntityPathBase<OrderInfo> {

    private static final long serialVersionUID = 1838655366L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderInfo orderInfo = new QOrderInfo("orderInfo");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath addressSub = createString("addressSub");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath deliveryCompany = createString("deliveryCompany");

    public final StringPath deliveryInvoice = createString("deliveryInvoice");

    public final StringPath deliveryMemo = createString("deliveryMemo");

    public final org.g9project4.member.entities.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath orderEmail = createString("orderEmail");

    public final QOrderItem orderItem;

    public final StringPath orderMobile = createString("orderMobile");

    public final StringPath orderName = createString("orderName");

    public final NumberPath<Long> orderNo = createNumber("orderNo", Long.class);

    public final StringPath payLog = createString("payLog");

    public final EnumPath<org.g9project4.payment.constants.PayMethod> payMethod = createEnum("payMethod", org.g9project4.payment.constants.PayMethod.class);

    public final StringPath payTid = createString("payTid");

    public final StringPath receiverMobile = createString("receiverMobile");

    public final StringPath receiverName = createString("receiverName");

    public final EnumPath<org.g9project4.order.constants.OrderStatus> status = createEnum("status", org.g9project4.order.constants.OrderStatus.class);

    public final StringPath zoneCode = createString("zoneCode");

    public QOrderInfo(String variable) {
        this(OrderInfo.class, forVariable(variable), INITS);
    }

    public QOrderInfo(Path<? extends OrderInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderInfo(PathMetadata metadata, PathInits inits) {
        this(OrderInfo.class, metadata, inits);
    }

    public QOrderInfo(Class<? extends OrderInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.g9project4.member.entities.QMember(forProperty("member")) : null;
        this.orderItem = inits.isInitialized("orderItem") ? new QOrderItem(forProperty("orderItem"), inits.get("orderItem")) : null;
    }

}

