package com.spring.user.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserUpdateDTO {

    private Long UserId;
    private String nickname;
    private String password;
    private String imageUrl;
}
