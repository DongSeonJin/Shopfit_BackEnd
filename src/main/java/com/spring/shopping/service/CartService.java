package com.spring.shopping.service;

import com.spring.shopping.DTO.CartDTO;
import com.spring.shopping.DTO.CartListResponseDTO;
import com.spring.shopping.entity.Product;
import com.spring.user.entity.User;

import java.util.List;

public interface CartService {

    // 장바구니에 추가
    CartDTO addToCart(CartDTO cartDTO);

    // 장바구니에서 제거
    void removeFromCart(Long cartId);

    // userId로 장바구니 목록 가져오기
    List<CartListResponseDTO> getUserCart(Long userId);

    // 어떤 유저의 장바구니에 이미 해당 상품이 있는지 확인하기
    boolean isProductInUserCart(Long userId, Long productId);

}
