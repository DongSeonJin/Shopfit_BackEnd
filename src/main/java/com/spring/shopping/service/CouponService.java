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


    // 총 쿠폰 발급 개수를 체크하고 쿠폰을 발급
    void confirm(Long userId);
    // 유저가 이미 쿠폰을 가지고있는지 확인
    public boolean checkCoupon(Long userId, String couponCode);


}
