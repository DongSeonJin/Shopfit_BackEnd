package com.spring.shopping.service;

import com.spring.shopping.DTO.CouponDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponService {
//    Optional<CouponDTO> getCouponById(Long couponId);
//    List<CouponDTO> getCouponsByUserId(Long userId);
//    List<CouponDTO> getExpiringCoupons(LocalDateTime dateTime);
//    List<CouponDTO> getAllCoupons();
//    void saveCoupon(CouponDTO couponDTO);
//    void deleteCoupon(Long couponId);


    // 총 쿠폰 발급 개수를 체크하고 쿠폰을 발급
    void confirm(Long userId);
    void reset();
    // 유저가 이미 쿠폰을 가지고있는지 확인
    boolean checkCoupon(Long userId, String couponCode);
    // 유저 ID로 쿠폰 조회
    List<CouponDTO> getCouponsByUserId(Long userId);
    // 쿠폰 사용
    // 결제하기 클릭 시 사용한 쿠폰 validTo를 현재 날짜로 수정하여 유효하지 않은 쿠폰으로 만들기.
    void expireCoupon(Long couponId);


}
