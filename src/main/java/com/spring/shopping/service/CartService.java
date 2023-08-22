package com.spring.shopping.service;

import com.spring.shopping.DTO.CartDTO;

import java.util.List;

public interface CartService {

    // 장바구니에 추가
    CartDTO addToCart(Long userId, Long productId, Long quantity);

    // 장바구니에서 제거
    void removeFromCart(Long cartId);

    // userId로 장바구니 목록 가져오기
    List<CartDTO> getUserCart(Long userId);

}
