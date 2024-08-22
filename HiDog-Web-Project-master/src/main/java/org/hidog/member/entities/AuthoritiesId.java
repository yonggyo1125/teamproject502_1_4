package org.hidog.member.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hidog.member.constants.Authority;

import java.io.Serializable;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritiesId implements Serializable {
    private Member member;
    private Authority authority;
}