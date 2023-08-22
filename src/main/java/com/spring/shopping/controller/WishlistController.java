package com.spring.shopping.controller;

import com.spring.shopping.DTO.WishlistDTO;
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
    public ResponseEntity<WishlistDTO> addToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        WishlistDTO wishlistItem = wishlistService.addToWishlist(userId, productId);
        if (wishlistItem != null) {
            return new ResponseEntity<>(wishlistItem, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove/{wishlistId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long wishlistId) {
        wishlistService.removeFromWishlist(wishlistId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistDTO>> getUserWishlist(@PathVariable Long userId) {
        List<WishlistDTO> wishlistItems = wishlistService.getUserWishlist(userId);
        return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
    }
}
