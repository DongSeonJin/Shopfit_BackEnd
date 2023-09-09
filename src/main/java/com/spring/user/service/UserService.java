package com.spring.user.service;

import com.spring.user.DTO.AddUserRequestDTO;
import com.spring.user.DTO.LoginRequestDTO;
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

    boolean authenticateUser(LoginRequestDTO loginRequest);

    boolean isNicknameAvailable(String nickname);

}
