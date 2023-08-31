package com.spring.community.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class LikeSaveDTO {

    private Long likeId;
    private String nickname;
    private Long userId;
    private Long postId;

}
