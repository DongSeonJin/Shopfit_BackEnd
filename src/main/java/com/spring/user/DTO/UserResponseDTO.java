package com.spring.user.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long userId;
    private String nickname;
    private Long point;
    private String imageUrl;

}
