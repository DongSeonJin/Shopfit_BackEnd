package com.spring.shopping.service;

import com.spring.shopping.DTO.CouponDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponService {
    Optional<CouponDTO> getCouponById(Long couponId);
    List<CouponDTO> getCouponsByUserId(Long userId);
    List<CouponDTO> getExpiringCoupons(LocalDateTime dateTime);
    List<CouponDTO> getAllCoupons();
    void saveCoupon(CouponDTO couponDTO);
    void deleteCoupon(Long couponId);
}
