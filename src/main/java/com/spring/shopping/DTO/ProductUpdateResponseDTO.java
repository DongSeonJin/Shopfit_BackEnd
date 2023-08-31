package com.spring.shopping.DTO;

import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.ProductImage;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductUpdateResponseDTO {

    private Long productId;
    private Long categoryId;
    private String productName;
    private String thumbnailUrl;
    private Long price;
    private Long stockQuantity;
    private List<String> productImageUrls;
    private List<Long> productImageIds;

    public static ProductUpdateResponseDTO toProductUpdateResponseDTO(Product product) {
        return ProductUpdateResponseDTO.builder()
                .productId(product.getProductId())
                .categoryId(product.getShopCategory().getCategoryId())
                .productName(product.getProductName())
                .thumbnailUrl(product.getThumbnailUrl())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .productImageUrls(getProductImageUrls(product.getProductImages())) // 아래 메서드 참고
                .productImageIds(getProductImageIds(product.getProductImages()))
                .build();
    }

    private static List<String> getProductImageUrls(List<ProductImage> productImages) {
        return productImages.stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
    }

    private static List<Long> getProductImageIds(List<ProductImage> productImages) {
        return productImages.stream()
                .map(ProductImage::getProductImageId)
                .collect(Collectors.toList());
    }

}
