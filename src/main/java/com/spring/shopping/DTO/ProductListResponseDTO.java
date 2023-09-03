package com.spring.shopping.DTO;

import com.spring.shopping.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponseDTO { // 상품 목록 조회 시 사용하는 DTO
    private Long productId;
    private Long categoryId;
    private String productName;
    private String thumbnailUrl;
    private Long price;
    private Long stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long reviewCount; // 정렬을 위한 리뷰 개수 변수

    public ProductListResponseDTO(Product product) { // 엔티티를 DTO로 바꾸는 생성자
        this.productId = product.getProductId();
        this.categoryId = product.getShopCategory().getCategoryId();
        this.productName = product.getProductName();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();

        // 리뷰 개수를 설정
        this.reviewCount = (long) product.getReviews().size();


    }
}
