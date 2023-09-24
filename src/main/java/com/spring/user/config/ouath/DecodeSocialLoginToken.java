package com.spring.user.config.ouath;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.exception.BusinessException;
import com.spring.exception.ExceptionCode;
import com.spring.user.DTO.LoginResponseDTO;
import com.spring.user.config.jwt.TokenProvider;
import com.spring.user.entity.Authority;
import com.spring.user.entity.RefreshToken;
import com.spring.user.entity.User;
import com.spring.user.repository.RefreshTokenRepository;
import com.spring.user.repository.UserRepository;
import com.spring.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class DecodeSocialLoginToken {

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;


    private final RefreshTokenRepository refreshTokenRepository;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7); // 이틀(리프레시 토큰의 유효기간)
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2); // 시간(억세스 토큰의 유효기간)

    @Value("${jwt.client_id}")
    private String CLIENT_ID;


    public LoginResponseDTO decodingToken(String token) throws IOException, GeneralSecurityException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();


        GoogleIdToken idToken = verifier.verify(token);



        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String name = (String) payload.get("name");  // name 정보 가져오기

//            System.out.println("디코딩된 토큰" + idToken.getPayload());

            String email = payload.getEmail();

//            System.out.println("얻은 이메일" + email);

            User user = userRepository.findByEmail(email) // JPA 는 기본 Optional객체 반환
                    .orElseGet(() -> { //없는 유저라면 null값이므로 새로 저장 로직. (람다식)
                        User newUser = User.builder()
                                .email(email)
                                .nickname(name)
                                .authority(Authority.USER)
                                .build();
                        newUser = userRepository.save(newUser);
                        return newUser; // if문 리턴이 아닌, user객체에 newUser 할당.
                    });

            // 엑세스 토큰 생성
            String accessToken = tokenProvider.generateAccessToken(user, ACCESS_TOKEN_DURATION);
            // 리프레시토큰 생성 및 저장
            String refreshToken = tokenProvider.generateRefreshToken(user, REFRESH_TOKEN_DURATION);

            RefreshToken newRefreshToken = new RefreshToken(user.getUserId(), refreshToken);
            // 객체에 담아서 저장
            refreshTokenRepository.save(newRefreshToken);





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
            throw new BusinessException(ExceptionCode.TOKEN_NOT_VALID);
        }




    }
}
