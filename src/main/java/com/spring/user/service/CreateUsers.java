//package com.spring.user.service;
//
//import com.spring.user.entity.User;
//import com.spring.user.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class CreateUsers implements CommandLineRunner {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Override
//    public void run(String... args) {
//        User user1 = User.builder()
//                .email("as@12.com")
//                .password(bCryptPasswordEncoder.encode("1111"))
//                .nickname("shopfit1")
//                .point(0)
//                .imageUrl("shopfit.jpg")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .isAdmin(false)
//                .build();
//
//        userRepository.save(user1);
//
//        System.out.println("Dummy users created!");
//    }
//}
