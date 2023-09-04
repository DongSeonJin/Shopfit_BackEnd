package com.spring.community.DTO;

import com.spring.community.entity.Post;
import lombok.*;

@NoArgsConstructor @Builder
@AllArgsConstructor @Getter @Setter
public class PostListByUserIdDTO {

    private Long userId;
    private Long postId;
    private String nickname;
    private Integer categoryId;
    private String title;
    private String content;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private Long viewCount;


    public PostListByUserIdDTO(Post post) {
        this.userId = post.getUser().getUserId();
        this.postId = post.getPostId();
        this.nickname = post.getUser().getNickname();
        this.categoryId = post.getPostCategory().getCategoryId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl1 = post.getImageUrl1();
        this.imageUrl2 = post.getImageUrl2();
        this.imageUrl3 = post.getImageUrl3();
        this.viewCount = post.getViewCount();
    }
}
