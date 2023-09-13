package com.spring.user.controller;

import com.spring.exception.CustomException;
import com.spring.user.DTO.*;
import com.spring.user.config.jwt.TokenProvider;
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
    public ResponseEntity<?> signup(@RequestBody AddUserRequestDTO requestDTO){

        return ResponseEntity.ok(userService.signup(requestDTO));
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

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }


     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
         // 폼에서 입력한 로그인 아이디를 이용해 DB에 저장된 전체 정보 얻어오기
        User userInfo = userService.getUserByEmail(loginRequest.getEmail());



         // 유저가 폼에서 날려주는 정보는 id랑 비번인데, 먼저 아이디를 통해 위에서 정보를 얻어오고
         // 비밀번호는 암호화 구문끼리 비교해야 하므로, 이 경우 bCryptEncoder의 .matchs(평문, 암호문) 를 이용하면
         // 같은 암호화 구문끼리 비교하는 효과가 생깁니다.
         // 상단에 bCryptPasswordEncoder 의존성을 생성한 후, if문 내부에서 비교합니다.

                                            // 폼에서 날려준 평문       // 디비에 들어있던 암호문
         if(bCryptPasswordEncoder.matches(loginRequest.getPassword(), userInfo.getPassword())){
             // 아이디와 비번을 모두 정확하게 입력한 사용자    에게 토큰 발급
             // 사용자 정보를 토대로, 2시간동안 유효한 억세스 토큰 생성
             String accessToken = tokenProvider.generateToken(userInfo, Duration.ofHours(2));
             String refreshToken = tokenProvider.generateToken(userInfo, Duration.ofDays(7));  // 예: 7일 동안 유요

             // json으로 리턴을 하고 싶으면, 클래스 요소를 리턴해야 합니다.
             TokenResponseDTO tokenDTO = new TokenResponseDTO(accessToken, refreshToken);

             // json으로 리턴을 하고 싶으면, 클래스 요소를 리턴해야 합니다.
             // AccessTokenResponseDTO 를 dto패키지에 생성합니다. 멤버변수로 accessToken만 가집니다.
             return ResponseEntity.ok(tokenDTO); // 발급 성공시 토큰 리턴
         } else {
             return ResponseEntity.badRequest().body("login failed"); // 비번이나 아이디 틀리면 로그인 실패
         }
     }


    // 회원 정보 조회
    @GetMapping("/mypage/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user != null) {
            UserResponseDTO userResponseDTO = UserResponseDTO.from(user);
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 회원 정보 수정
    @PatchMapping("/mypage/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {

        userUpdateDTO.setUserId(id);
        try {
            userService.updateUser(userUpdateDTO);
            return new ResponseEntity<>("유저 정보가 성공적으로 업데이트되었습니다.", HttpStatus.OK);
        } catch (UserIdNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("유저 정보 업데이트 중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            // userId로 사용자 정보를 조회
            User user = userService.getUserById(userId);

            // 포인트 사용 로직 호출
            boolean success = userService.usePoints(user, usedPoints);

            if (success) {
                // 성공적으로 처리된 경우
                return ResponseEntity.ok("포인트가 성공적으로 사용되었습니다.");
            } else {
                // 포인트가 부족한 경우
                return ResponseEntity.badRequest().body("포인트가 부족합니다.");
            }
        } catch (UserIdNotFoundException e) {
            // 사용자를 찾을 수 없는 경우
            return ResponseEntity.notFound().build();
        }
    }




}
