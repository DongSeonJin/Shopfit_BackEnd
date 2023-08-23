package com.spring.shopping.repository;

import com.spring.shopping.entity.Coupon;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findById(Long couponId);
    List<Coupon> findByCategory(String category);
    List<Coupon> findByExpirationDateBefore(Date date);
    List<Coupon> findAll();
    void save(Coupon coupon);
    void delete(String couponId);

    void deleteById(Long couponId);
}
