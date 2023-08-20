package com.spring.shopping.controller;

import com.spring.shopping.entity.Wishlist;
import com.spring.shopping.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add")
    public ResponseEntity<Wishlist> addItemToWishlist(@RequestBody Wishlist wishlistItem) {
        Wishlist addedItem = wishlistService.addItemToWishlist(wishlistItem);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{wishlistId}")
    public ResponseEntity<String> removeItemFromWishlist(@PathVariable Long wishlistId) {
        wishlistService.removeItemFromWishlist(wishlistId);
        return new ResponseEntity<>("Item removed from wishlist", HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getUserWishlist(@PathVariable Long userId) {
        List<Wishlist> userWishlist = wishlistService.getUserWishlist(userId);
        return new ResponseEntity<>(userWishlist, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Wishlist>> getProductWishlist(@PathVariable Long productId) {
        List<Wishlist> productWishlist = wishlistService.getProductWishlist(productId);
        return new ResponseEntity<>(productWishlist, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}/likeCount")
    public ResponseEntity<Integer> getProductLikeCount(@PathVariable Long productId) {
        int likeCount = wishlistService.getProductLikeCount(productId);
        return new ResponseEntity<>(likeCount, HttpStatus.OK);
    }
}
