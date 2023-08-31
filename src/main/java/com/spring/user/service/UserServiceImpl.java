package com.spring.user.service;

import com.spring.user.DTO.AddUserRequestDTO;
import com.spring.user.DTO.LoginRequestDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.User;
import com.spring.user.exception.UserIdNotFoundException;
import com.spring.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


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

        User updateUser = userRepository.findById(userUpdateDTO.getUserId())
                .orElseThrow(() -> new UserIdNotFoundException("해당되는 글을 찾을 수 없습니다 : " + userUpdateDTO.getUserId()));

        updateUser.builder()
                .nickname(userUpdateDTO.getNickname())
                .password(userUpdateDTO.getPassword())
                .imageUrl(userUpdateDTO.getImageUrl())
                .build();

        userRepository.save(updateUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean authenticateUser(LoginRequestDTO loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            // 인증 성공
            return true;
        }
        // 인증 실패
        return false;
    }

}