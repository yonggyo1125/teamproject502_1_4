package org.g9project4.board.advices;

import lombok.RequiredArgsConstructor;
import org.g9project4.wishlist.constants.WishType;
import org.g9project4.wishlist.services.WishListService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice("org.g9project4.board")
@RequiredArgsConstructor
public class BoardControllerAdvice {
    private final WishListService wishListService;

    @ModelAttribute("boardWishList")
    public List<Long> boardWishList() {
        return wishListService.getList(WishType.BOARD);
    }

    @ModelAttribute("tourWishList")
    public List<Long> tourWishList() {
        return wishListService.getList(WishType.TOUR);
    }
}
