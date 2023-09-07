package com.spring.user.controller;

import com.spring.user.DTO.AddUserRequestDTO;
import com.spring.user.DTO.LoginRequestDTO;
import com.spring.user.DTO.UserUpdateDTO;
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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;
    private final UserDetailService userDetailService;


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody AddUserRequestDTO requestDTO){
        userService.save(requestDTO); // 현재 userService에 email, pw뿐이라 수정 요망
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

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }


//     @PostMapping("/login")
//     public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
//         boolean isAuthenticated = userService.authenticateUser(loginRequest);

//         if (isAuthenticated) {
//             return ResponseEntity.ok("Login successful");
//         } else {
//             return ResponseEntity.status(401).body("Login failed");
//         }
//     }


    // 회원 정보 조회
    @GetMapping("/mypage/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 회원 정보 수정
    @PatchMapping("/mypage")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.toString();

        try {
            userUpdateDTO.toString();
            userService.updateUser(userUpdateDTO);
            return new ResponseEntity<>("유저 정보가 성공적으로 업데이트되었습니다.", HttpStatus.OK);
        } catch (UserIdNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("유저 정보 업데이트 중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
