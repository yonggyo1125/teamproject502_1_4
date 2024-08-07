package org.g9project4.member.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g9project4.member.constants.Authority;

import java.io.Serializable;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritiesId implements Serializable {
    private Member member;
    private Authority authority;
}
