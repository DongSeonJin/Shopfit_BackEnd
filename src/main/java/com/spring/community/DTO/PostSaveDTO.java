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

    private User user;
    private String nickname;
    private PostCategory postCategory;
    private String title;
    private String content;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;


    public Post toPost() { //toPost -> toEntity로 변경
        return Post.builder()
                .user(user)
                .nickname(nickname)
                .postCategory(postCategory)
                .title(title)
                .content(content)
                .imageUrl1(imageUrl1)
                .imageUrl2(imageUrl2)
                .imageUrl3(imageUrl3)
                .build();
    }
}
