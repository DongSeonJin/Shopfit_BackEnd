package com.spring.shopping.service;

import com.spring.shopping.entity.Wishlist;

import java.util.List;

public interface WishlistService {

    // Wishlist에 아이템 추가
    Wishlist addItemToWishlist(Wishlist wishlistItem);

    // Wishlist에서 아이템 삭제
    void removeItemFromWishlist(Long wishlistId);

    // 특정 유저의 Wishlist 조회
    List<Wishlist> getUserWishlist(Long userId);

    // 특정 상품의 Wishlist 조회; 상품 인기도 분석, 제품 추천 및 마케팅 등
    List<Wishlist> getProductWishlist(Long productId);

    int getProductLikeCount(Long productId);
}
