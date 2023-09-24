package com.spring.user.controller;

import com.spring.user.DTO.*;
import com.spring.user.config.jwt.TokenProvider;
import com.spring.user.config.ouath.DecodeSocialLoginToken;
import com.spring.user.entity.User;
import com.spring.user.service.UserDetailService;
import com.spring.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;

    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TokenProvider tokenProvider;

    private final DecodeSocialLoginToken decodeSocialLoginToken;



    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signup(@RequestBody AddUserRequestDTO requestDTO){
        userService.signup(requestDTO); // 위 로직 전부 서비스레이어로 옮김
        return ResponseEntity.ok("회원가입 성공");
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken,
                                         @RequestBody String refreshToken){
            userService.logout(accessToken, refreshToken);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }


     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponseDTO = userService.login(loginRequest);

        return ResponseEntity.ok(loginResponseDTO);
     }

     @PostMapping("/login/google")
     public ResponseEntity<?> googleLogin(@RequestBody String token) throws GeneralSecurityException, IOException {

         return ResponseEntity.ok(decodeSocialLoginToken.decodingToken(token));
     }


    // 회원 정보 조회
    @GetMapping("/mypage/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

            UserResponseDTO userResponseDTO = UserResponseDTO.from(user);
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }

    // 회원 정보 수정
    @PatchMapping("/mypage/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {

        userUpdateDTO.setUserId(id);
            userService.updateUser(userUpdateDTO);
            return new ResponseEntity<>("유저 정보가 성공적으로 업데이트되었습니다.", HttpStatus.OK);

    }

    // 닉네임 중복 검증
    @PostMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody NicknameDTO nicknameDTO) {
        String nickname = nicknameDTO.getNickname();
        boolean isNicknameAvailable = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(isNicknameAvailable);
    }

    //유저 포인트 조회
    @GetMapping("/{userId}/point")
    public ResponseEntity<UserPointResponseDTO> getUserPoint(@PathVariable Long userId) {
        UserPointResponseDTO userPoint = userService.getUserPointById(userId);
        return ResponseEntity.ok(userPoint); // HTTP 200 OK
    }

    // 유저 포인트 사용 시 포인트 수정
    @PatchMapping("/{userId}/usePoints/{usedPoints}")
    public ResponseEntity<String> usePoints(
            @PathVariable Long userId,
            @PathVariable int usedPoints
    ) {
        // userId로 사용자 정보를 조회
        User user = userService.getUserById(userId);

        // 포인트 사용 로직 호출
        userService.usePoints(user, usedPoints);

        return ResponseEntity.ok("포인트가 성공적으로 사용되었습니다.");
    }




}
