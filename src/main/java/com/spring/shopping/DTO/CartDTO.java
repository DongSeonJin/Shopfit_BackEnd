package com.spring.shopping.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private Long cartId;
    private Long userId; // User 엔티티의 id를 저장
    private Long productId; // Product 엔티티의 id를 저장
    private Long quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
