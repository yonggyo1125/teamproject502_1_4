package org.g9project4.publicData.tour.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSigunguCode is a Querydsl query type for SigunguCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSigunguCode extends EntityPathBase<SigunguCode> {

    private static final long serialVersionUID = 1429568906L;

    public static final QSigunguCode sigunguCode1 = new QSigunguCode("sigunguCode1");

    public final StringPath areaCode = createString("areaCode");

    public final StringPath name = createString("name");

    public final StringPath sigunguCode = createString("sigunguCode");

    public QSigunguCode(String variable) {
        super(SigunguCode.class, forVariable(variable));
    }

    public QSigunguCode(Path<? extends SigunguCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSigunguCode(PathMetadata metadata) {
        super(SigunguCode.class, metadata);
    }

}

