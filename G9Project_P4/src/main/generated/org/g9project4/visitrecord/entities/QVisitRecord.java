package org.g9project4.visitrecord.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVisitRecord is a Querydsl query type for VisitRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVisitRecord extends EntityPathBase<VisitRecord> {

    private static final long serialVersionUID = -1906350376L;

    public static final QVisitRecord visitRecord = new QVisitRecord("visitRecord");

    public final NumberPath<Long> contentId = createNumber("contentId", Long.class);

    public final NumberPath<Integer> uid = createNumber("uid", Integer.class);

    public final NumberPath<Long> visitCount = createNumber("visitCount", Long.class);

    public final DatePath<java.time.LocalDate> yearMonth = createDate("yearMonth", java.time.LocalDate.class);

    public QVisitRecord(String variable) {
        super(VisitRecord.class, forVariable(variable));
    }

    public QVisitRecord(Path<? extends VisitRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVisitRecord(PathMetadata metadata) {
        super(VisitRecord.class, metadata);
    }

}

