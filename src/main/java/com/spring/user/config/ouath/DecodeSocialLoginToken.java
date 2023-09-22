package com.spring.user.config.ouath;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.user.DTO.LoginResponseDTO;
import com.spring.user.config.jwt.TokenProvider;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import com.spring.user.service.TokenService;
import com.spring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class DecodeSocialLoginToken {
    
    private final UserService userService;

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;

    private final TokenService tokenService;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7); // 이틀(리프레시 토큰의 유효기간)
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2); // 시간(억세스 토큰의 유효기간)
    private final static String CLIENT_ID = "711393533645-css3t7bs3k4e5fhl3pnmgqn6nj6or42s.apps.googleusercontent.com";

    public LoginResponseDTO decodingToken(String token) throws IOException, GeneralSecurityException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);


        User user = null;
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String name = (String) payload.get("name");  // name 정보 가져오기

            System.out.println("디코딩된 토큰" + idToken.getPayload());

            String email = payload.getEmail();

            System.out.println("얻은 이메일" + email);

            // 이메일로 유저 정보 조회 후 null일 시 유저 정보 저장.
            user = userRepository.findByEmail(email).orElse(userRepository.save(User.builder()
                    .email(email)
                    .nickname(name).build()));

            // 리프레시 토큰 생성 및 저장 로직...

        } else {
            System.out.println("Invalid ID token.");
        }



        // 리프레시토큰 생성 및 저장
        String refreshToken = tokenProvider.generateRefreshToken(user, REFRESH_TOKEN_DURATION);
        // 리프레시토큰 이미 존재한다면 업데이트, 없다면 저장 함수
        tokenService.updateRefreshToken(user);
        // 엑세스 토큰 생성 및 저장
        String accessToken = tokenProvider.generateAccessToken(user, ACCESS_TOKEN_DURATION);



        return new LoginResponseDTO(user, refreshToken, accessToken);
    }
}
