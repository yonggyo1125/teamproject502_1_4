package org.g9project4.wishlist.repositories;

import org.g9project4.wishlist.entities.WishList;
import org.g9project4.wishlist.entities.WishListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WishListRepository extends JpaRepository<WishList, WishListId>, QuerydslPredicateExecutor<WishList> {
}
