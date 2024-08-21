package org.hidog.global.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMemberEntity extends BaseEntity implements Serializable {// 로그인한 사용자
    @CreatedBy
    @Column(updatable = false, length = 65)
    private String createdBy;

    @LastModifiedBy
    @Column(length = 65, insertable = false)
    private String modifiedBy;
}
