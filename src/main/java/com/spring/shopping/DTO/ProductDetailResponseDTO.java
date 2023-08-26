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
    private String categoryName;
    private String productName;
    private String thumbnailUrl;
    private Long price;
    private Long stockQuantity;
    private List<String> productImageUrls;
    private List<ProductDetailReviewResponseDTO> reviews;



    // static으로 from 메서드를 만드는 방식 : 주로 데이터 변환 로직에 사용된다.
    // 엔터티를 DTO로 변환
    // Product에서 가져온 정보를 상품 상세 정보 조회에 필요한 DTO에 설정하기 위한 메서드
    public static ProductDetailResponseDTO from(Product product) {
        ProductDetailResponseDTO dto = new ProductDetailResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setCategoryId(product.getShopCategory().getCategoryId());
        dto.setCategoryName(product.getShopCategory().getCategoryName());
        dto.setProductName(product.getProductName());
        dto.setThumbnailUrl(product.getThumbnailUrl());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());

        // 제품 이미지 URL 목록을 생성
        List<String> productImageUrls = product.getProductImages().stream()
                .map(ProductImage::getImageUrl) // 각각의 ProductImage 엔터티에서 이미지 url 가져오기
                .collect(Collectors.toList()); // 스트림의 결과를 리스트로 반환하기
        dto.setProductImageUrls(productImageUrls); // 제품 이미지 URL 목록 설정

        // 제품 리뷰 목록을 생성
        List<ProductDetailReviewResponseDTO> reviews = product.getReviews().stream()
                .map(ProductDetailReviewResponseDTO::from) // 각각의 review를 from 메서드로 DTO로 변환하기
                .collect(Collectors.toList()); // 스트림의 결과를 리스트로 반환하기
        dto.setReviews(reviews); // 제품 리뷰 목록 설정

        return dto; // 완성된 DTO를 반환
    }

}
