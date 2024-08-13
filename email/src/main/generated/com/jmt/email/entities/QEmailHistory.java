package com.jmt.email.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailHistory is a Querydsl query type for EmailHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailHistory extends EntityPathBase<EmailHistory> {

    private static final long serialVersionUID = 71847017L;

    public static final QEmailHistory emailHistory = new QEmailHistory("emailHistory");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath message = createString("message");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath subject = createString("subject");

    public final StringPath to = createString("to");

    public QEmailHistory(String variable) {
        super(EmailHistory.class, forVariable(variable));
    }

    public QEmailHistory(Path<? extends EmailHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailHistory(PathMetadata metadata) {
        super(EmailHistory.class, metadata);
    }

}

