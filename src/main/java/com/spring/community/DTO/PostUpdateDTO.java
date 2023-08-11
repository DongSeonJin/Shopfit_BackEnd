package com.spring.community.DTO;

import com.spring.community.entity.Post;
import lombok.*;

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

    public PostUpdateDTO(Post post){
        this.postId = post.getPostId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
