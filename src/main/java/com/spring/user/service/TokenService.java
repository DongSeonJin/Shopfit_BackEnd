package com.spring.user.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.user.DTO.RefreshTokenRotationDTO;
import com.spring.user.config.jwt.TokenProvider;
import com.spring.user.entity.RefreshToken;
import com.spring.user.entity.User;
import com.spring.user.repository.RefreshTokenRepository;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenRotationDTO createNewAccessToken(String refreshToken) {
        // !!!! 리프레시 토큰도 JWT스펙으로 만들어지기 때문에, TokenProvider에 의한 유효성 검증이 가능합니다. !!!!
        if(!tokenProvider.validToken(refreshToken)){
            throw new CustomException(ExceptionCode.TOKEN_NOT_VALID); // 재로그인 요청 메시지 전달
        }// 유효하지 않은 토큰이면 예외 발생후 종료
        // 리프레시토큰이 유효하지 않다면, 다시 발급해줘야하지만 로그인 로직에 발급 기능이 있고, 재로그인을 요구한다면 필수는 아니다.
        System.out.println("refreshToken : " + refreshToken);

        Long userId = refreshTokenRepository.findUserIdByRefreshToken(refreshToken);

        User user = userRepository.findById(userId).get();




        // 유효한 토큰이면 어떤 유저의 토큰인지 먼저 확인
//        String userId = refreshTokenRepository.findByRefreshToken(refreshToken);

//        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_FOUND));
        //유효하다면 refreshToken과 accessToken 둘다 만들고 리턴
        String newRefreshToken = updateRefreshToken(user); //refreshToken은 DB도 갱신해야하므로 하단에 메서드 분리.
        String newAccessToken = tokenProvider.generateAccessToken(user, Duration.ofHours(2));



        // 리프레시 토큰이 유효하며, 유저가 존재한다면 새로운 억세스 토큰을 발급합니다.
        return new RefreshTokenRotationDTO(newRefreshToken, newAccessToken);
    }


    public String updateRefreshToken(User userInfo){
        String refreshToken = tokenProvider.generateRefreshToken(userInfo, Duration.ofDays(7));//7일동안 유효한 리프레시토큰 발급


        //새로운 리프레시토큰 refreshToken 엔터티 객체에 담기
        RefreshToken newRefreshToken = new RefreshToken(userInfo.getUserId(), refreshToken);
        // DB에서 userId에 해당하는 refresh토큰 검색
        Optional<RefreshToken> existingToken = Optional.ofNullable(refreshTokenRepository.findByUserId(userInfo.getUserId()));

        // 기존 토근이 있든 없든 갱신하거나 새로 저장합니다.
        refreshTokenRepository.save(existingToken.orElse(newRefreshToken).update(newRefreshToken.getRefreshToken()));
        return refreshToken;
    }
}
