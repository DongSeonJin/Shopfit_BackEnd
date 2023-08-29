package com.spring.community.DTO;

import com.spring.community.entity.Post;
import com.spring.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostUpdateDTO {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;

    public PostUpdateDTO(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.updatedAt = post.getUpdatedAt();
        this.imageUrl1 = post.getImageUrl1();
        this.imageUrl2 = post.getImageUrl2();
        this.imageUrl3 = post.getImageUrl3();
    }


}