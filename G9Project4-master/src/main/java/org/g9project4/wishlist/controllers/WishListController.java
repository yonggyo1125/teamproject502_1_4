package org.g9project4.wishlist.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.exceptions.RestExceptionProcessor;
import org.g9project4.wishlist.constants.WishType;
import org.g9project4.wishlist.services.WishListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wish")
@RequiredArgsConstructor
public class WishListController implements RestExceptionProcessor {
    private final WishListService service;

    //추가
    @GetMapping("/{type}/{seq}")
    public ResponseEntity<Void> add(@PathVariable("type") String type, @PathVariable("seq") Long seq) {

        service.add(seq, WishType.valueOf(type));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //제거
    @DeleteMapping("/{type}/{seq}")
    public ResponseEntity<Void> remove(@PathVariable("type") String type, @PathVariable("seq") Long seq) {
        service.remove(seq, WishType.valueOf(type));

        return ResponseEntity.ok().build();
    }
}
