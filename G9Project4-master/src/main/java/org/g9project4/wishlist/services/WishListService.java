package org.g9project4.wishlist.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.g9project4.member.MemberUtil;
import org.g9project4.member.entities.Member;
import org.g9project4.search.entities.SearchHistory;
import org.g9project4.wishlist.constants.WishType;
import org.g9project4.wishlist.entities.QWishList;
import org.g9project4.wishlist.entities.WishList;
import org.g9project4.wishlist.entities.WishListId;
import org.g9project4.wishlist.repositories.WishListRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

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

    public List<Long> getList(WishType type) {
        if (!memberUtil.isLogin()) {
            return null;
        }

        BooleanBuilder builder = new BooleanBuilder();
        QWishList wishList = QWishList.wishList;
        builder.and(wishList.member.eq(memberUtil.getMember()))
                .and(wishList.wishType.eq(type));

        List<Long> items = ((List<WishList>)repository.findAll(builder, Sort.by(desc("createdAt")))).stream().map(WishList::getSeq).toList();

        return items;
    }

    public boolean check(Long seq, String  type) {
        if (memberUtil.isLogin()) {
            WishListId wishListId = new WishListId(seq, WishType.valueOf(type), memberUtil.getMember());

            return repository.existsById(wishListId);
        }

        return false;
    }

}
