package com.spring.community.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostTop4DTO {
    private String title;
    private String imageUrl;
    private Long postId;
    private Long viewCount;
}
