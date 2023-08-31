package com.spring.shopping.DTO;

import com.spring.shopping.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartListResponseDTO {

    private Long cartId;
    private Long userId;
    private Long productId;
    private String productName;
    private String thumbnailUrl;
    private Long stockQuantity;
    private Long price;
    private Long quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 엔터티를 DTO로 변환하는 메서드
    public static CartListResponseDTO from(Cart cart) {
        CartListResponseDTO dto = new CartListResponseDTO();
        dto.setCartId(cart.getCartId());
        dto.setUserId(cart.getUser().getUserId());
        dto.setProductId(cart.getProduct().getProductId());
        dto.setProductName(cart.getProduct().getProductName());
        dto.setThumbnailUrl(cart.getProduct().getThumbnailUrl());
        dto.setStockQuantity(cart.getProduct().getStockQuantity());
        dto.setPrice(cart.getProduct().getPrice());
        dto.setQuantity(cart.getQuantity());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        return dto;
    }


}
