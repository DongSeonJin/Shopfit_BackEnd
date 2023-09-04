package com.spring.user.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequestDTO {
    private String email;
    private String password;
    private String nickname;
    private String imageUrl;
}
