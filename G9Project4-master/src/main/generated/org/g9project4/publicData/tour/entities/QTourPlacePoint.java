package org.g9project4.publicData.tour.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTourPlacePoint is a Querydsl query type for TourPlacePoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourPlacePoint extends EntityPathBase<TourPlacePoint> {

    private static final long serialVersionUID = -1283741380L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTourPlacePoint tourPlacePoint = new QTourPlacePoint("tourPlacePoint");

    public final NumberPath<Integer> finalPointValueAge = createNumber("finalPointValueAge", Integer.class);

    public final NumberPath<Integer> finalPointValueInterest = createNumber("finalPointValueInterest", Integer.class);

    public final NumberPath<Integer> finalPointValueVisit = createNumber("finalPointValueVisit", Integer.class);

    public final org.g9project4.member.entities.QMember member;

    public final QTourPlace tourPlace;

    public QTourPlacePoint(String variable) {
        this(TourPlacePoint.class, forVariable(variable), INITS);
    }

    public QTourPlacePoint(Path<? extends TourPlacePoint> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTourPlacePoint(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTourPlacePoint(PathMetadata metadata, PathInits inits) {
        this(TourPlacePoint.class, metadata, inits);
    }

    public QTourPlacePoint(Class<? extends TourPlacePoint> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.g9project4.member.entities.QMember(forProperty("member")) : null;
        this.tourPlace = inits.isInitialized("tourPlace") ? new QTourPlace(forProperty("tourPlace")) : null;
    }

}

