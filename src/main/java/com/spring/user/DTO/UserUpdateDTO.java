package com.spring.user.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserUpdateDTO {

    private Long userId;
    private String nickname;
    private String imageUrl;
}
