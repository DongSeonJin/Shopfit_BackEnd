package com.spring.user.repository;

import com.spring.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByUserId(Long userId); // 유저번호로 토큰정보 얻기
    RefreshToken findByRefreshToken(String refreshToken); // 토큰으로 토큰정보 얻기
    void deleteByUserId(Long userId);

    void deleteByRefreshToken(String refreshToken);

    @Query("SELECT r.userId FROM RefreshToken r WHERE r.refreshToken = :refreshToken")
    Long findUserIdByRefreshToken(String refreshToken);

}
