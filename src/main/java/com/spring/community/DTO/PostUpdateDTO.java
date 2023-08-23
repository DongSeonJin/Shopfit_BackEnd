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

    public PostUpdateDTO(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.updatedAt = post.getUpdatedAt();
    }


}