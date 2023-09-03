package com.spring.community.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class LikeRequestDTO {

    private Long userId;
    private Long postId;


}
