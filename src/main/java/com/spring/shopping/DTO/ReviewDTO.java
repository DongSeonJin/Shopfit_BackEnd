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
public class ReviewDTO {
    private Long reviewId;
    private Long userId;
    private Long productId;
    private Double rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
