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

    private long postId;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime updatedAt;

    public PostUpdateDTO(Post post){
        this.postId = post.getPostId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.updatedAt = post.getUpdatedAt();
    }


}