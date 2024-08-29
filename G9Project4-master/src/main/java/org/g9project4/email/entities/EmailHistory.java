package org.g9project4.email.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EmailHistory {
    @Id @GeneratedValue
    private Long seq;

    @Column(length=100, name="_to")
    private String to;

    private String subject;

    @Lob
    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}