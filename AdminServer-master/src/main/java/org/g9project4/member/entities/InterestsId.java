package org.g9project4.member.entities;

import lombok.*;
import org.g9project4.member.constants.Interest;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class InterestsId implements Serializable {

    private Member member;
    private Interest interest;

}
