package com.spring.user.DTO;

import com.spring.user.entity.Authority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequestDTO {
    private String email;
    private String password;
    private String nickname;
    private String imageUrl;
    private String confirmPassword;
    private Authority Authority;
}
