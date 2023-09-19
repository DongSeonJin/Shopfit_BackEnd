package com.spring.user.DTO;

import com.spring.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String refreshToken;
    private String accessToken;
    private Long userId;
    private String email;
    private String nickname;
    private String authority;

    public LoginResponseDTO(User user){
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.authority = String.valueOf(user.getAuthority());
    }
}
