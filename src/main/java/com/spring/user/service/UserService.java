package com.spring.user.service;

import com.spring.user.DTO.AddUserRequestDTO;
import com.spring.user.DTO.LoginRequestDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(AddUserRequestDTO dto);

    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    void updateUser(UserUpdateDTO userUpdateDTO);
    void deleteUserById(Long id);

    boolean authenticateUser(LoginRequestDTO loginRequest);

}
