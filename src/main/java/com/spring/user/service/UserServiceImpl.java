package com.spring.user.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.user.DTO.AddUserRequestDTO;
import com.spring.user.DTO.LoginRequestDTO;
import com.spring.user.DTO.UserPointResponseDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.Authority;
import com.spring.user.entity.User;
import com.spring.user.exception.UserIdNotFoundException;
import com.spring.user.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public User signup(AddUserRequestDTO dto) { // 회원 email, password를 저장하고 password는 암호화

        boolean existEmail = userRepository.existsByEmail(dto.getEmail()); // 존재하는 이메일인지 확인
        User userNickname = userRepository.findByNickname(dto.getNickname());

        String password = dto.getPassword();
        String passwordCheck = dto.getConfirmPassword();

        if(!password.equals(passwordCheck)){
            throw new CustomException(ExceptionCode.PASSWORD_WRONG);
        }

        if(existEmail){
            throw new CustomException(ExceptionCode.EXIST_EMAIL);
        }

        if(userNickname != null){
            throw new CustomException(ExceptionCode.EXIST_NICKNAME);
        }


        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .imageUrl(dto.getImageUrl())
                .authority(dto.getAuthority())
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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.USER_ID_NOT_FOUND));
        return user;
    }

    // 유저 정보 수정
    @Override
    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {

        User user = userRepository.findById(userUpdateDTO.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_ID_NOT_FOUND));

        User updatingUser = User.builder()
                .userId(userUpdateDTO.getUserId())
                .email(user.getEmail())
                .nickname(userUpdateDTO.getNickname())
                .password(user.getPassword())
                .imageUrl(userUpdateDTO.getImageUrl())
                .point(user.getPoint())
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(updatingUser);
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

    // 중복 닉네임 검증
    @Override
    public boolean isNicknameAvailable(String nickname) {
        // 닉네임으로 회원 조회
        User existingUser = userRepository.findByNickname(nickname);
        // 중복되지 않으면 true, 중복되면 false를 반환합니다.
        return existingUser == null;
    }

    // 유저 포인트 조회
    @Override
    public UserPointResponseDTO getUserPointById(Long userId) {
        // 유저 ID로 유저 정보를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_ID_NOT_FOUND));


        // 조회한 유저 정보에서 포인트를 가져와서 DTO에 담아 반환
        return new UserPointResponseDTO(user.getUserId(), user.getPoint());

    }

    // 유저 포인트 사용 시 포인트 수정
    @Override
    public boolean usePoints(User user, int usedPoints) {
        int currentPoints = user.getPoint();

        if(currentPoints < usedPoints) {
            throw new CustomException(ExceptionCode.INSUFFICIENT_POINT_EXCEPTION);
        }

        int updatedPoints = currentPoints - usedPoints;

        // 엔티티 객체 복제
        User updatingUser = User.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .imageUrl(user.getImageUrl())
                .point(updatedPoints) // 포인트만 업데이트
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(updatingUser); // 수정된 유저 정보 저장
        return true;

    }



}