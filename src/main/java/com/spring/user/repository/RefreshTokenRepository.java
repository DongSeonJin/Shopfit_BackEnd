package com.spring.user.repository;

import com.spring.user.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByUserId(Long userId); // 유저번호로 토큰정보 얻기
    @Query("SELECT r FROM RefreshToken r WHERE r.refreshToken = :refreshToken")
    RefreshToken findByRefreshToken(@Param("refreshToken") String refreshToken);
    void deleteByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken r WHERE r.refreshToken = :refreshToken")
    void deleteByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query("SELECT r.userId FROM RefreshToken r WHERE r.refreshToken = :refreshToken")
    Long findUserIdByRefreshToken(String refreshToken);

}
