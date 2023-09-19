package com.spring.user.service;

import com.spring.user.DTO.*;
import com.spring.user.entity.User;

import java.util.List;

public interface UserService {
    void signup(AddUserRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    User createUser(User user);
    List<User> getAllUsers();

    User getUserByNickname(String nickname);

    User getUserByEmail(String email);

    User getUserById(Long id);
    void updateUser(UserUpdateDTO userUpdateDTO);
    void deleteUser(Long id);
    UserPointResponseDTO getUserPointById(Long userId);
    boolean usePoints(User user, int usedPoints);
    boolean authenticateUser(LoginRequestDTO loginRequest);

    boolean isNicknameAvailable(String nickname);

}
