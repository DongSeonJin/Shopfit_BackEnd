package com.spring.shopping.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductTop3DTO {
    private Long productId;
    private String productName;
    private String thumbnailUrl;
    private Long price;
}
