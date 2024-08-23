package org.g9project4.wishlist.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.wishlist.entities.WishList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final MemberUtil memberUtil;

    public void add(Long seq, WishList type) {

    }

    public void remove(Long seq, WishList type) {

    }

    public List<Long> getList(WishList type) {

        return null;
    }
}
