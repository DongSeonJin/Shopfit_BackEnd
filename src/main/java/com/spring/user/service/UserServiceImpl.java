package com.spring.user.service;

import com.spring.user.DTO.AddUserRequestDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{


     UserRepository userRepository;
     BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void save(AddUserRequestDTO dto) { // 회원 email, password를 저장하고 password는 암호화

         userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()
         );
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {

        User updateUser = userRepository.findById(userUpdateDTO.getUserId()).get();

        updateUser.update(userUpdateDTO);

        userRepository.save(updateUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}