package com.spring.user.service;

import com.spring.user.DTO.AddUserRequestDTO;
import com.spring.user.DTO.LoginRequestDTO;
import com.spring.user.DTO.UserPointResponseDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.User;

import java.util.List;

public interface UserService {
    String signup(AddUserRequestDTO dto);

    User createUser(User user);
    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getUserById(Long id);
    void updateUser(UserUpdateDTO userUpdateDTO);
    void deleteUserById(Long id);
    UserPointResponseDTO getUserPointById(Long userId);
    boolean usePoints(User user, int usedPoints);
    boolean authenticateUser(LoginRequestDTO loginRequest);

    boolean isNicknameAvailable(String nickname);

}
