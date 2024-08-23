package org.g9project4.wishlist.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.wishlist.constants.WishType;
import org.g9project4.wishlist.entities.WishList;
import org.g9project4.wishlist.entities.WishListId;
import org.g9project4.wishlist.repositories.WishListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final MemberUtil memberUtil;
    private final WishListRepository repository;

    public void add(Long seq, WishType type) {
        if (!memberUtil.isLogin()) {
            return;
        }

        WishList wishList = WishList.builder()
                .wishType(type)
                .seq(seq)
                .member(memberUtil.getMember())
                .build();
        repository.saveAndFlush(wishList);
    }

    public void remove(Long seq, WishType type) {
        if (!memberUtil.isLogin()) {
            return;
        }

        WishListId wishListId = new WishListId(seq, type, memberUtil.getMember());
        repository.deleteById(wishListId);
        repository.flush();
    }

    public List<Long> getList(WishList type) {

        return null;
    }
}
