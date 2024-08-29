package org.g9project4.publicData.tourvisit.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSidoVisit is a Querydsl query type for SidoVisit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSidoVisit extends EntityPathBase<SidoVisit> {

    private static final long serialVersionUID = 1010961884L;

    public static final QSidoVisit sidoVisit = new QSidoVisit("sidoVisit");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    public final StringPath areaCode = createString("areaCode");

    public final StringPath areaName = createString("areaName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Double> type1D1 = createNumber("type1D1", Double.class);

    public final NumberPath<Double> type1M1 = createNumber("type1M1", Double.class);

    public final NumberPath<Double> type1M3 = createNumber("type1M3", Double.class);

    public final NumberPath<Double> type1M6 = createNumber("type1M6", Double.class);

    public final NumberPath<Double> type1W1 = createNumber("type1W1", Double.class);

    public final NumberPath<Double> type1Y1 = createNumber("type1Y1", Double.class);

    public final NumberPath<Double> type2D1 = createNumber("type2D1", Double.class);

    public final NumberPath<Double> type2M1 = createNumber("type2M1", Double.class);

    public final NumberPath<Double> type2M3 = createNumber("type2M3", Double.class);

    public final NumberPath<Double> type2M6 = createNumber("type2M6", Double.class);

    public final NumberPath<Double> type2W1 = createNumber("type2W1", Double.class);

    public final NumberPath<Double> type2Y1 = createNumber("type2Y1", Double.class);

    public final NumberPath<Double> type3D1 = createNumber("type3D1", Double.class);

    public final NumberPath<Double> type3M1 = createNumber("type3M1", Double.class);

    public final NumberPath<Double> type3M3 = createNumber("type3M3", Double.class);

    public final NumberPath<Double> type3M6 = createNumber("type3M6", Double.class);

    public final NumberPath<Double> type3W1 = createNumber("type3W1", Double.class);

    public final NumberPath<Double> type3Y1 = createNumber("type3Y1", Double.class);

    public QSidoVisit(String variable) {
        super(SidoVisit.class, forVariable(variable));
    }

    public QSidoVisit(Path<? extends SidoVisit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSidoVisit(PathMetadata metadata) {
        super(SidoVisit.class, metadata);
    }

}

