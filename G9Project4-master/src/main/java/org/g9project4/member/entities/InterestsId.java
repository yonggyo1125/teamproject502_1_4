package org.g9project4.member.entities;

import jakarta.persistence.*;
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


//    public void setMember(Member member) {
//        this.memberSeq = member.getSeq();
//    }

}
