package org.g9project4.search.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSearchHistory is a Querydsl query type for SearchHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchHistory extends EntityPathBase<SearchHistory> {

    private static final long serialVersionUID = 1651500494L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSearchHistory searchHistory = new QSearchHistory("searchHistory");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath keyword = createString("keyword");

    public final org.g9project4.member.entities.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> searchCount = createNumber("searchCount", Long.class);

    public final EnumPath<org.g9project4.search.constatnts.SearchType> searchType = createEnum("searchType", org.g9project4.search.constatnts.SearchType.class);

    public QSearchHistory(String variable) {
        this(SearchHistory.class, forVariable(variable), INITS);
    }

    public QSearchHistory(Path<? extends SearchHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSearchHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSearchHistory(PathMetadata metadata, PathInits inits) {
        this(SearchHistory.class, metadata, inits);
    }

    public QSearchHistory(Class<? extends SearchHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.g9project4.member.entities.QMember(forProperty("member")) : null;
    }

}

