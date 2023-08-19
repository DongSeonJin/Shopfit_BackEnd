package com.spring.shopping.DTO;

import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDTO {
    private Long productId;
    private Long categoryId;
    private String productName;
    private String thumbnailUrl;
    private Long price;
    private Long stockQuantity;
    private List<String> productImageUrls;
    private List<ProductDetailReviewResponseDTO> reviews;

    public static ProductDetailResponseDTO from(Product product) {
        ProductDetailResponseDTO dto = new ProductDetailResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setCategoryId(product.getCategory().getCategoryId());
        dto.setProductName(product.getProductName());
        dto.setThumbnailUrl(product.getThumbnailUrl());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());

        List<String> productImageUrls = product.getProductImages().stream()
                .map(ProductImage::getImageUrl) // 각각의 ProductImage 엔터티에서 이미지 url 가져오기
                .collect(Collectors.toList()); // 스트림의 결과를 리스트로 반환하기
        dto.setProductImageUrls(productImageUrls);

        List<ProductDetailReviewResponseDTO> reviews = product.getReviews().stream()
                .map(ProductDetailReviewResponseDTO::from) // 각각의 review를 from 메서드로 DTO로 변환하기
                .collect(Collectors.toList()); // 스트림의 결과를 리스트로 반환하기
        dto.setReviews(reviews);

        return dto;
    }

}
