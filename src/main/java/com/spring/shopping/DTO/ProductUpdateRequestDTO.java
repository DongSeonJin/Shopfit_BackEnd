package com.spring.shopping.DTO;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductUpdateRequestDTO {

    private Long productId;
    private Long categoryId;
    private String productName;
    private String thumbnailUrl;
    private Long price;
    private Long stockQuantity;
    private List<String> productImageUrls;

    @Override
    public String toString() {
        return "ProductUpdateRequestDTO{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", productName='" + productName + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", productImageUrls=" + productImageUrls +
                '}';
    }

}
