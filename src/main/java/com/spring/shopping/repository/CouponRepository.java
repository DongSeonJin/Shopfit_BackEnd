package com.spring.shopping.repository;

import com.spring.shopping.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
//    Optional<Coupon> findById(Long couponId);
//    List<Coupon> findByCategory(String category);
//    List<Coupon> findByExpirationDateBefore(Date date);
//    List<Coupon> findAll();
//    void save(Coupon coupon);
//    void delete(String couponId);
//    void deleteById(Long couponId);

    Optional<Coupon> findByUser_UserIdAndCouponCode(Long userId, String CouponCode);

    List<Coupon> findByUser_UserId(Long userId);


}
