package org.g9project4.wishlist.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.member.entities.Member;
import org.g9project4.wishlist.constants.WishType;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(WishListId.class)
public class WishList extends BaseEntity {
    @Id
    private Long seq;

    @Id
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private WishType wishType;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
