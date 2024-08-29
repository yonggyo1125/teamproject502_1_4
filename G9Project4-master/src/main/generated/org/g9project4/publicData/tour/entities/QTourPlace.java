package org.g9project4.publicData.tour.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTourPlace is a Querydsl query type for TourPlace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourPlace extends EntityPathBase<TourPlace> {

    private static final long serialVersionUID = 1713343668L;

    public static final QTourPlace tourPlace = new QTourPlace("tourPlace");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath areaCode = createString("areaCode");

    public final BooleanPath bookTour = createBoolean("bookTour");

    public final StringPath category1 = createString("category1");

    public final StringPath category2 = createString("category2");

    public final StringPath category3 = createString("category3");

    public final NumberPath<Long> contentId = createNumber("contentId", Long.class);

    public final NumberPath<Long> contentTypeId = createNumber("contentTypeId", Long.class);

    public final StringPath cpyrhtDivCd = createString("cpyrhtDivCd");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> createdTime = createDateTime("createdTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Double> distance = createNumber("distance", Double.class);

    public final NumberPath<Integer> finalPointValue = createNumber("finalPointValue", Integer.class);

    public final StringPath firstImage = createString("firstImage");

    public final StringPath firstImage2 = createString("firstImage2");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Integer> mapLevel = createNumber("mapLevel", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final DateTimePath<java.time.LocalDateTime> modifiedTime = createDateTime("modifiedTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> placePointValue = createNumber("placePointValue", Integer.class);

    public final StringPath sigunguCode = createString("sigunguCode");

    public final StringPath tel = createString("tel");

    public final StringPath title = createString("title");

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

    public QTourPlace(String variable) {
        super(TourPlace.class, forVariable(variable));
    }

    public QTourPlace(Path<? extends TourPlace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTourPlace(PathMetadata metadata) {
        super(TourPlace.class, metadata);
    }

}

