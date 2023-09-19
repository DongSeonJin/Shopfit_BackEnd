package com.spring.user.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.user.DTO.UserPointResponseDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.Authority;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
//@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    // 중복 닉네임 검증 테스트
    @Test
    public void isNicknameAvailableTest() {
        // given: 더미 데이터 설정
        String nonExistingNickname = "nono";

        // when : 메서드 실행
        boolean nicknameAvailable = userService.isNicknameAvailable(nonExistingNickname);

        // then : 결과 검증
        assertTrue(nicknameAvailable);
    }

    // 유저 포인트 조회 테스트
    @Test
    public void testGetUserPointById() {
        // given: 더미 데이터 설정
        Long existingUserId = 1001L;
        int userPoint = 10000;

        User user = new User(existingUserId, "aa@aa.aa", "abcde", "nick1", userPoint, "test", LocalDateTime.now(), LocalDateTime.now(), Authority.USER, null);

        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(user));

        // when : 메서드 실행
        UserPointResponseDTO result = userService.getUserPointById(existingUserId);

        // then : 결과 검증
        assertEquals(existingUserId, result.getUserId());
        assertEquals(userPoint, result.getPoint());
    }

    // 포인트 사용 테스트
    @Test
    public void testUsePoints() {
        // given: 더미 데이터 설정
        Long userId = 1001L;
        int initialPoints = 10000;
        int usedPoints = 5000;

        User user = new User(userId, "aa@aa.aa", "abcde", "nick1", initialPoints, "test", LocalDateTime.now(), LocalDateTime.now(), Authority.USER, null);

        // userRepository의 save() 메소드가 어떤 User 객체로든 호출되면, 그대로 해당 User 객체를 반환하라
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // when : 메서드 실행
        boolean result = userService.usePoints(user, usedPoints);

        // then : 결과 검증
        assertTrue(result);
    }

}

