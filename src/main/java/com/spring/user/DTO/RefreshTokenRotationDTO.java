package com.spring.user.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RefreshTokenRotationDTO {
    private String refreshToken;
    private String accessToken;


}
