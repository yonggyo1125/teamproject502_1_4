package org.g9project4.member.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInterests is a Querydsl query type for Interests
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterests extends EntityPathBase<Interests> {

    private static final long serialVersionUID = 819563865L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInterests interests = new QInterests("interests");

    public final EnumPath<org.g9project4.member.constants.Interest> interest = createEnum("interest", org.g9project4.member.constants.Interest.class);

    public final QMember member;

    public QInterests(String variable) {
        this(Interests.class, forVariable(variable), INITS);
    }

    public QInterests(Path<? extends Interests> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInterests(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInterests(PathMetadata metadata, PathInits inits) {
        this(Interests.class, metadata, inits);
    }

    public QInterests(Class<? extends Interests> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

