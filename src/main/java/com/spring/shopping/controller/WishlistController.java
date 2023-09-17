package com.spring.shopping.controller;

import com.spring.shopping.DTO.ProductTop3DTO;
import com.spring.shopping.DTO.WishlistDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.service.ProductService;
import com.spring.shopping.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;

    @Autowired
    public WishlistController(WishlistService wishlistService, ProductService productService) {
        this.wishlistService = wishlistService;
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<WishlistDTO> addToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        WishlistDTO wishlistItem = wishlistService.addToWishlist(userId, productId);
//        if (wishlistItem != null) {
            return new ResponseEntity<>(wishlistItem, HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } 예외 처리 관련 로직 서비스 레이어에서 처리함
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        WishlistDTO wishlistItem = wishlistService.removeFromWishlist(userId, productId);
//        if (wishlistItem != null) {
            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } 예외 처리를 서비스 레이어에서 하도록 수정하였음
    }

    // wishlist 선택 삭제
    @DeleteMapping("/{wishlistId}")
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
    public ResponseEntity<List<ProductTop3DTO>> getTop3Products() {
        Map<Long, Long> productRowCountMap = wishlistService.getProductRowCountMap();

        List<ProductTop3DTO> top3Products = productRowCountMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(3)
                .map(entry -> {
                    Long productId = entry.getKey();
                    Product product = productService.getProductInfo(productId);
                    return new ProductTop3DTO(
                            product.getProductId(),
                            product.getProductName(),
                            product.getThumbnailUrl(),
                            product.getPrice()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(top3Products);
    }

}
