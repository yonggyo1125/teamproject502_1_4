package org.g9project4.publicData.tourvisit.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSigunguVisit is a Querydsl query type for SigunguVisit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSigunguVisit extends EntityPathBase<SigunguVisit> {

    private static final long serialVersionUID = -152293407L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSigunguVisit sigunguVisit = new QSigunguVisit("sigunguVisit");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QSidoVisit sidoVisit;

    public final StringPath sigunguCode = createString("sigunguCode");

    public final StringPath sigunguName = createString("sigunguName");

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

    public QSigunguVisit(String variable) {
        this(SigunguVisit.class, forVariable(variable), INITS);
    }

    public QSigunguVisit(Path<? extends SigunguVisit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSigunguVisit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSigunguVisit(PathMetadata metadata, PathInits inits) {
        this(SigunguVisit.class, metadata, inits);
    }

    public QSigunguVisit(Class<? extends SigunguVisit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sidoVisit = inits.isInitialized("sidoVisit") ? new QSidoVisit(forProperty("sidoVisit")) : null;
    }

}

