package org.g9project4.publicData.tourvisit.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSigunguTable is a Querydsl query type for SigunguTable
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSigunguTable extends EntityPathBase<SigunguTable> {

    private static final long serialVersionUID = -154395036L;

    public static final QSigunguTable sigunguTable = new QSigunguTable("sigunguTable");

    public final StringPath sidoCode = createString("sidoCode");

    public final StringPath sigunguCode2 = createString("sigunguCode2");

    public final StringPath sigunguName = createString("sigunguName");

    public QSigunguTable(String variable) {
        super(SigunguTable.class, forVariable(variable));
    }

    public QSigunguTable(Path<? extends SigunguTable> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSigunguTable(PathMetadata metadata) {
        super(SigunguTable.class, metadata);
    }

}

