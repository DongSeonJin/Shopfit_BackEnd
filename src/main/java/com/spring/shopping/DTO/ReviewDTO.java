package com.spring.shopping.DTO;

import com.google.protobuf.MessageLite;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long userId;
    private Long productId;
    private Double rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
