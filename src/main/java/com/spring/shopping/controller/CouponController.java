package com.spring.shopping.controller;

import com.spring.shopping.DTO.CouponDTO;
import com.spring.shopping.service.CouponService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }


    // 쿠폰 발급
    @PostMapping("/confirm/{userId}")
    public ResponseEntity<String> confirmCoupon(@PathVariable long userId) {

            couponService.confirm(userId);

            return ResponseEntity.ok("쿠폰이 발급되었습니다.");
    }

    // 어떤 유저가 이미 해당 쿠폰을 가지고 있는지 확인하기
    @GetMapping("/checkCoupon/{userId}/{couponCode}")
    public ResponseEntity<Boolean> checkCoupon(@PathVariable Long userId, @PathVariable String couponCode) {

        boolean isCoupon = couponService.checkCoupon(userId, couponCode);

        return ResponseEntity.ok(isCoupon);
    }

    // 특정 사용자 ID에 해당하는 모든 쿠폰 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<CouponDTO>> getCouponsByUserId(@PathVariable Long userId) {
        List<CouponDTO> coupons = couponService.getCouponsByUserId(userId);
        return ResponseEntity.ok(coupons);
    }

    // 쿠폰 사용 - 쿠폰 사용 시 해당 쿠폰 만료시키기
    @PostMapping("/expire/{couponId}")
    public void expireCoupon(@PathVariable Long couponId) {
        couponService.expireCoupon(couponId);
    }



}
