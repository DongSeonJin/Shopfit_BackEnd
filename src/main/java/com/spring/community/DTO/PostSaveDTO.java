package com.spring.community.DTO;

import com.spring.community.entity.Post;
import com.spring.community.entity.PostCategory;
import com.spring.user.entity.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostSaveDTO {

    private Long userId;
    private String nickname;
    private Integer categoryId;
    private String title;
    private String content;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;

    @Builder.Default
    private Long viewCount = 0L;


    public PostSaveDTO(Post post){
        this.userId = post.getUser().getUserId();
        this.nickname = post.getUser().getNickname();
        this.categoryId = post.getPostCategory().getCategoryId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl1 = post.getImageUrl1();
        this.imageUrl2 = post.getImageUrl2();
        this.imageUrl3 = post.getImageUrl3();
        this.viewCount = post.getViewCount() == null? 0:post.getViewCount();
    }



}
