package com.spring.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.user.DTO.NicknameDTO;
import com.spring.user.DTO.UserPointResponseDTO;
import com.spring.user.DTO.UserResponseDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.Authority;
import com.spring.user.entity.User;
import com.spring.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @MockBean
    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void signupTest() throws Exception {
    }


    // 회원 정보 조회 테스트
    @Test
    public void testGetUserById() throws Exception {
        // given: 더미 데이터 설정
        Long userId = 1001L;

        User user = new User(userId, "aa@aa.aa", "abcdefghijk", "nick1", 10000, "test", LocalDateTime.now(), LocalDateTime.now(), Authority.USER, null);

        // when & then: getUserById 호출 및 반환된 결과 검증
        mockMvc.perform(get("/mypage/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // 회원 정보 수정 테스트
    @Test
    @Transactional
    public void testUpdateUser() throws Exception {
        // given: 더미 데이터 설정
        Long userId = 1001L;
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        userUpdateDTO.setNickname("test2");

        ObjectMapper objectMapper = new ObjectMapper();

        // when & then: updateUser 호출 및 반환된 결과 검증
        mockMvc.perform(patch("/mypage/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("유저 정보가 성공적으로 업데이트되었습니다."));
    }


    // 닉네임 중복검증 테스트
    @Test
    public void testCheckNickname() throws Exception {
        // given: 더미 데이터 설정
        String nickname = "test";
        NicknameDTO nicknameDTO = new NicknameDTO();
        nicknameDTO.setNickname(nickname);

        ObjectMapper objectMapper = new ObjectMapper();

        // when & then: checkNickname 호출 및 반환된 결과 검증
        mockMvc.perform(post("/checkNickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nicknameDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // 유저 포인트 조회 테스트
    @Test
    public void testGetUserPoint() throws Exception {
        // given: 더미 데이터 설정
        Long userId = 1001L;
        UserPointResponseDTO userPointResponseDTO = new UserPointResponseDTO(userId, 5000);

        ObjectMapper objectMapper = new ObjectMapper();

        // when & then: getUserPoint 호출 및 반환된 결과 검증
        mockMvc.perform(get("/{userId}/point", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //유저 포인트 사용 시 포인트 수정 테스트
    @Test
    @Transactional
    public void testUsePoints() throws Exception {
        // given: 더미 데이터 설정
        Long userId = 1001L;
        int usedPoints = 5000;

        User user = new User(userId, "aa@aa.aa", "abcdefghijk", "nick1", 10000, "test", LocalDateTime.now(), LocalDateTime.now(), Authority.USER, null);

        // when & then: usePoints 호출 및 반환된 결과 검증
        mockMvc.perform(patch("/{userId}/usePoints/{usedPoints}", userId, usedPoints)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("포인트가 성공적으로 사용되었습니다."));
    }


}
