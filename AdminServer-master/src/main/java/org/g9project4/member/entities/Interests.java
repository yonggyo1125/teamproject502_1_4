package org.g9project4.member.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.member.constants.Interest;

import java.io.Serializable;

@Data
@Entity
@Builder
@IdClass(InterestsId.class)
@Table(name = "interests")
@NoArgsConstructor
public class Interests implements Serializable {


    @Id
    @ManyToOne
    @JoinColumn(name = "member_seq")
    private Member member;


    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private Interest interest;

    public Interests(Member member, Interest interest) {
        this.member = member;
        this.interest = interest;
    }
}
