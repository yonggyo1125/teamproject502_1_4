package org.g9project4.publicData.tour.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAreaCode is a Querydsl query type for AreaCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAreaCode extends EntityPathBase<AreaCode> {

    private static final long serialVersionUID = 996119637L;

    public static final QAreaCode areaCode1 = new QAreaCode("areaCode1");

    public final StringPath areaCode = createString("areaCode");

    public final StringPath name = createString("name");

    public QAreaCode(String variable) {
        super(AreaCode.class, forVariable(variable));
    }

    public QAreaCode(Path<? extends AreaCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAreaCode(PathMetadata metadata) {
        super(AreaCode.class, metadata);
    }

}

