package com.spring.user.config.ouath;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.user.DTO.LoginResponseDTO;
import com.spring.user.config.jwt.TokenProvider;
import com.spring.user.entity.Authority;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import com.spring.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DecodeSocialLoginToken {

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;

    private final TokenService tokenService;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7); // 이틀(리프레시 토큰의 유효기간)
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2); // 시간(억세스 토큰의 유효기간)
    public static final String CLIENT_ID = "711393533645-css3t7bs3k4e5fhl3pnmgqn6nj6or42s.apps.googleusercontent.com";


    public LoginResponseDTO decodingToken(String token) throws IOException, GeneralSecurityException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();


        GoogleIdToken idToken = verifier.verify(token);
        System.out.println("idToken은 :" + idToken);



        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String name = (String) payload.get("name");  // name 정보 가져오기

            System.out.println("디코딩된 토큰" + idToken.getPayload());

            String email = payload.getEmail();

            System.out.println("얻은 이메일" + email);

            User user = userRepository.findByEmail(email) // JPA 는 기본 Optional객체 반환
                    .orElseGet(() -> { //없는 유저라면 null값이므로 새로 저장 로직. (람다식)
                        User newUser = User.builder()
                                .email(email)
                                .nickname(name)
                                .authority(Authority.USER)
                                .build();
                        userRepository.save(newUser);
                        return newUser;
                    });


            // 리프레시토큰 생성 및 저장
            String refreshToken = tokenProvider.generateRefreshToken(user, REFRESH_TOKEN_DURATION);
            // 리프레시토큰 이미 존재한다면 업데이트, 없다면 저장 함수
            tokenService.updateRefreshToken(user);
            // 엑세스 토큰 생성 및 저장
            String accessToken = tokenProvider.generateAccessToken(user, ACCESS_TOKEN_DURATION);



            return LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .authority(String.valueOf(user.getAuthority()))
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();


        } else {
            System.out.println("Invalid ID token.");
            throw new CustomException(ExceptionCode.TOKEN_NOT_VALID);
        }




    }
}
