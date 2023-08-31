package com.spring.shopping.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductSaveRequestDTO {

    private Long categoryId;
    private String productName;
    private String thumbnailUrl;
    private Long price;
    private Long stockQuantity;

    private List<String> productImageUrls;


}
