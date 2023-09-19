package com.spring.user.controller;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.user.DTO.*;
import com.spring.user.config.jwt.TokenProvider;
import com.spring.user.entity.Authority;
import com.spring.user.entity.User;
import com.spring.user.exception.UserIdNotFoundException;
import com.spring.user.service.UserDetailService;
import com.spring.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;

    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TokenProvider tokenProvider;




    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signup(@RequestBody AddUserRequestDTO requestDTO){
//        User userEmail = userService.getUserByEmail(requestDTO.getEmail()); // 존재하는 이메일인지 확인
//        User userNickname = userService.getUserByNickname(requestDTO.getNickname());
//
//        String password = requestDTO.getPassword();
//        String passwordCheck = requestDTO.getConfirmPassword();
//
//        requestDTO.setAuthority(Authority.valueOf("USER")); // 회원가입은 기본 USER
//
//        if(!password.equals(passwordCheck)){
//            throw new CustomException(ExceptionCode.PASSWORD_WRONG);
//        }
//
//        if(userEmail != null){
//            throw new CustomException(ExceptionCode.EXIST_EMAIL);
//        }
//
//        if(userNickname != null){
//            throw new CustomException(ExceptionCode.EXIST_NICKNAME);
//        }
//
//        User user = userService.signup(requestDTO);

        userService.signup(requestDTO); // 위 로직 전부 서비스레이어로 옮김



        return ResponseEntity.ok("회원가입 성공");
    }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public ResponseEntity<?> login(@RequestBody AddUserRequestDTO addUserRequestDTO){
//        String requestEmail = addUserRequestDTO.getEmail();
//        String requestPW = addUserRequestDTO.getPassword();
//
//        String userPW = userDetailService.loadUserByUsername(requestEmail).getPassword();
//
//        return ResponseEntity.ok("로그인 성공");
//
//    }

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


    // 회원 정보 조회
    @GetMapping("/mypage/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

//        if (user != null) {
            UserResponseDTO userResponseDTO = UserResponseDTO.from(user);
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } 예외 처리를 서비스 레이어에서 하도록 수정함
    }

    // 회원 정보 수정
    @PatchMapping("/mypage/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {

        userUpdateDTO.setUserId(id);
//        try {
            userService.updateUser(userUpdateDTO);
            return new ResponseEntity<>("유저 정보가 성공적으로 업데이트되었습니다.", HttpStatus.OK);
//        } catch (UserIdNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>("유저 정보 업데이트 중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
//        } 예외 처리를 서비스 레이어에서 하도록 수정함
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
