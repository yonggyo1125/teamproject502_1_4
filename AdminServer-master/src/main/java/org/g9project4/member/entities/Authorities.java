package org.g9project4.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.member.constants.Authority;

import java.io.Serializable;

@Data
@Entity
@Builder
@IdClass(AuthoritiesId.class)
@NoArgsConstructor @AllArgsConstructor
public class Authorities implements Serializable {
    @Id
    @JsonIgnore
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Member member;

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private Authority authority;
}