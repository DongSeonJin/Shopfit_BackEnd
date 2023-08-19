package com.spring.shopping.DTO;

import com.spring.shopping.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailReviewResponseDTO {
    private Long reviewId;
    private Long userId;
    private Double rating;
    private String comment;

    // 엔터티를 DTO로 변환하는 메서드
    public static ProductDetailReviewResponseDTO from(Review review) {
        ProductDetailReviewResponseDTO dto = new ProductDetailReviewResponseDTO();
        dto.setReviewId(review.getReviewId());
        dto.setUserId(review.getUser().getUserId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        return dto;
    }
}
