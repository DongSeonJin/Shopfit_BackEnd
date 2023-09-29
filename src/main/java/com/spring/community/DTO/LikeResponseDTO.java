package com.spring.community.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class LikeResponseDTO {
    private Long likeCnt;
    private int isLiked;

}
