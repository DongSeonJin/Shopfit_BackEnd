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



}
