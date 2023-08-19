package com.spring.community.DTO;

import com.spring.community.entity.Post;
import com.spring.user.entity.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LikeSaveDTO {

    private Long likeId;
    private String nickname;
    private User user;
    private Post post;

}
