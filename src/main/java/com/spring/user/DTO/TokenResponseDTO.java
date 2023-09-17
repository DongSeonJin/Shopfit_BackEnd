package com.spring.user.DTO;

import com.spring.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private User user;

    public TokenResponseDTO(User user){
        this.accessToken = getAccessToken();
        this.refreshToken = getRefreshToken();
        this.user = getUser();
    }
}
