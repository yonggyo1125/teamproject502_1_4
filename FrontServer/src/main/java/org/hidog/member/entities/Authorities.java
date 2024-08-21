package org.hidog.member.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hidog.member.constants.Authority;

import java.io.Serializable;

@Data
@Entity
@Builder
@IdClass(AuthoritiesId.class)
@NoArgsConstructor @AllArgsConstructor
public class Authorities implements Serializable {
    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    private Member member;

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private Authority authority;
}