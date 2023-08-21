package com.spring.shopping.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDTO {

    private Long wishlistId;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}
