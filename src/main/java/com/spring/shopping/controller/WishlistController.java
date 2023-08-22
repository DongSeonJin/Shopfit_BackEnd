package com.spring.shopping.controller;

import com.spring.shopping.DTO.WishlistDTO;
import com.spring.shopping.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/top3")
    public ResponseEntity<List<Long>> getTop3Products() {
        Map<Long, Long> productRowCountMap = wishlistService.getProductRowCountMap();

        // 등장 횟수가 가장 많은 상위 3개 상품을 가져옵니다
        List<Long> top3Products = productRowCountMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return ResponseEntity.ok(top3Products);
    }
}
