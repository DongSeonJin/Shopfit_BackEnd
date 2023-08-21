package com.spring.community.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LikeSaveDTO {

    private Long likeId;
    private String nickname;
    private Long userId;
    private Long postId;

}
