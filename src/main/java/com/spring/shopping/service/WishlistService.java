package com.spring.shopping.service;

import com.spring.shopping.DTO.WishlistDTO;
import com.spring.shopping.entity.Product;

import java.util.List;
import java.util.Map;

public interface WishlistService {

    WishlistDTO addToWishlist(Long userId, Long productId);

    void removeFromWishlist(Long wishlistId);

    List<WishlistDTO> getUserWishlist(Long userId);

    Map<Long, Long> getProductRowCountMap();
}
