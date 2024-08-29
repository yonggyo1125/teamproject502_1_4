package org.g9project4.publicData.tour.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGreenPlace is a Querydsl query type for GreenPlace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGreenPlace extends EntityPathBase<GreenPlace> {

    private static final long serialVersionUID = -1228107457L;

    public static final QGreenPlace greenPlace = new QGreenPlace("greenPlace");

    public final StringPath address = createString("address");

    public final StringPath areacode = createString("areacode");

    public final StringPath contentId = createString("contentId");

    public final StringPath contentType = createString("contentType");

    public final StringPath cpyrhtDivCd = createString("cpyrhtDivCd");

    public final StringPath firstImage = createString("firstImage");

    public final DateTimePath<java.time.LocalDateTime> modifiedtime = createDateTime("modifiedtime", java.time.LocalDateTime.class);

    public final StringPath sigunguCode = createString("sigunguCode");

    public final StringPath subTitle = createString("subTitle");

    public final StringPath summary = createString("summary");

    public final StringPath tel = createString("tel");

    public final StringPath telName = createString("telName");

    public final StringPath title = createString("title");

    public QGreenPlace(String variable) {
        super(GreenPlace.class, forVariable(variable));
    }

    public QGreenPlace(Path<? extends GreenPlace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGreenPlace(PathMetadata metadata) {
        super(GreenPlace.class, metadata);
    }

}

