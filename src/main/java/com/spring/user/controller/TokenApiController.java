package com.spring.user.controller;

import com.spring.user.DTO.RefreshTokenRequestDTO;
import com.spring.user.DTO.AccesssTokenResponseDTO;
import com.spring.user.DTO.RefreshTokenRotationDTO;
import com.spring.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")                                        // json으로 보내야 파라미터 매핑됨
    public ResponseEntity<RefreshTokenRotationDTO> createNewAccessToken(@RequestHeader("Authorization") String refreshToken){
        // 전달받은 리프레시 토큰을 이용해 새로운 억세스 토큰 발급
        RefreshTokenRotationDTO refreshTokenRotation = tokenService.createNewAccessToken(refreshToken);

        // 발급된 토큰을 201 응답(생성됨) 과 함께 리턴
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenRotation);
    }
}
