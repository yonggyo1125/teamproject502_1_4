package org.g9project4.board.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentData is a Querydsl query type for CommentData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentData extends EntityPathBase<CommentData> {

    private static final long serialVersionUID = 1685421563L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentData commentData = new QCommentData("commentData");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    public final QBoardData boardData;

    public final StringPath commenter = createString("commenter");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath guestPw = createString("guestPw");

    public final StringPath ip = createString("ip");

    public final org.g9project4.member.entities.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath ua = createString("ua");

    public QCommentData(String variable) {
        this(CommentData.class, forVariable(variable), INITS);
    }

    public QCommentData(Path<? extends CommentData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentData(PathMetadata metadata, PathInits inits) {
        this(CommentData.class, metadata, inits);
    }

    public QCommentData(Class<? extends CommentData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.boardData = inits.isInitialized("boardData") ? new QBoardData(forProperty("boardData"), inits.get("boardData")) : null;
        this.member = inits.isInitialized("member") ? new org.g9project4.member.entities.QMember(forProperty("member")) : null;
    }

}

