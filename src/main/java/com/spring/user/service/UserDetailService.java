package com.spring.user.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {


    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email) // 로그인 아이디 입력되면 회원정보 리턴
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
    }
}
