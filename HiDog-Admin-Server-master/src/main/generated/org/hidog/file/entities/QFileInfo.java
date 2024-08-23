package org.hidog.file.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileInfo is a Querydsl query type for FileInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileInfo extends EntityPathBase<FileInfo> {

    private static final long serialVersionUID = 1770148226L;

    public static final QFileInfo fileInfo = new QFileInfo("fileInfo");

    public final org.hidog.global.entities.QBaseMemberEntity _super = new org.hidog.global.entities.QBaseMemberEntity(this);

    public final StringPath contentType = createString("contentType");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final BooleanPath done = createBoolean("done");

    public final StringPath extension = createString("extension");

    public final StringPath fileName = createString("fileName");

    public final StringPath gid = createString("gid");

    public final StringPath location = createString("location");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public QFileInfo(String variable) {
        super(FileInfo.class, forVariable(variable));
    }

    public QFileInfo(Path<? extends FileInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileInfo(PathMetadata metadata) {
        super(FileInfo.class, metadata);
    }

}

