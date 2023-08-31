package com.spring.community.DTO;

import com.spring.community.entity.Post;
import com.spring.community.entity.Reply;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @ToString @Builder
public class IndividualPostResponseDTO {

    private Long postId;
    private String nickname;
    private String category;
    private String title;
    private String content;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String reply;

    public IndividualPostResponseDTO (Post post) {
        this.postId = post.getPostId();
        this.nickname = post.getNickname();
        this.category = post.getPostCategory().getCategoryName();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.imageUrl1 = post.getImageUrl1();
        this.imageUrl2 = post.getImageUrl2();
        this.imageUrl3 = post.getImageUrl3();

    }


}
