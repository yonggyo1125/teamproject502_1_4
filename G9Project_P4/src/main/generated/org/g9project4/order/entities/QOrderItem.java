package org.g9project4.order.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderItem is a Querydsl query type for OrderItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItem extends EntityPathBase<OrderItem> {

    private static final long serialVersionUID = 1838661099L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderItem orderItem = new QOrderItem("orderItem");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    public final org.g9project4.board.entities.QBoardData boardData;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath itemName = createString("itemName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QOrderInfo orderInfo;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> qty = createNumber("qty", Integer.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public QOrderItem(String variable) {
        this(OrderItem.class, forVariable(variable), INITS);
    }

    public QOrderItem(Path<? extends OrderItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderItem(PathMetadata metadata, PathInits inits) {
        this(OrderItem.class, metadata, inits);
    }

    public QOrderItem(Class<? extends OrderItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.boardData = inits.isInitialized("boardData") ? new org.g9project4.board.entities.QBoardData(forProperty("boardData"), inits.get("boardData")) : null;
        this.orderInfo = inits.isInitialized("orderInfo") ? new QOrderInfo(forProperty("orderInfo"), inits.get("orderInfo")) : null;
    }

}

