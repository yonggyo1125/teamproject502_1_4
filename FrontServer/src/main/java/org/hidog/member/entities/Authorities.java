package org.hidog.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
    @JsonIgnore @ToString.Exclude
    private Member member;

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private Authority authority;
}