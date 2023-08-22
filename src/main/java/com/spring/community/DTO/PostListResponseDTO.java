package com.spring.community.DTO;

import com.spring.community.entity.Post;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostListResponseDTO {

    private String nickname;
    private String title;
    private LocalDateTime updatedAt;
    private Long viewCount;
    private String imageUrl1;
    //좋아요도 묶어서 조회

    public PostListResponseDTO(Post post) {
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.updatedAt = post.getUpdatedAt();
        this.viewCount = post.getViewCount();
        this.imageUrl1 = post.getImageUrl1();
    }


}
