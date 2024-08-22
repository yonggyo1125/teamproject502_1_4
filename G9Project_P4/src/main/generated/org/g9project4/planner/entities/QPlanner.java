package org.g9project4.planner.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanner is a Querydsl query type for Planner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanner extends EntityPathBase<Planner> {

    private static final long serialVersionUID = -1087895816L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanner planner = new QPlanner("planner");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final DatePath<java.time.LocalDate> eDate = createDate("eDate", java.time.LocalDate.class);

    public final StringPath itinerary = createString("itinerary");

    public final org.g9project4.member.entities.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final DatePath<java.time.LocalDate> sDate = createDate("sDate", java.time.LocalDate.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath title = createString("title");

    public QPlanner(String variable) {
        this(Planner.class, forVariable(variable), INITS);
    }

    public QPlanner(Path<? extends Planner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanner(PathMetadata metadata, PathInits inits) {
        this(Planner.class, metadata, inits);
    }

    public QPlanner(Class<? extends Planner> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.g9project4.member.entities.QMember(forProperty("member")) : null;
    }

}

