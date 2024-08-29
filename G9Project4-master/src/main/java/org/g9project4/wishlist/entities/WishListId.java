package org.g9project4.wishlist.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g9project4.member.entities.Member;
import org.g9project4.wishlist.constants.WishType;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class WishListId {
    private Long seq;
    private WishType wishType;
    private Member member;
}
