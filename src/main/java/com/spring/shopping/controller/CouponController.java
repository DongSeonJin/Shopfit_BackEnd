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

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Long couponId) {
        Optional<CouponDTO> coupon = couponService.getCouponById(couponId);
        return coupon.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CouponDTO>> getCouponsByUserId(@PathVariable Long userId) {
        List<CouponDTO> coupons = couponService.getCouponsByUserId(userId);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<CouponDTO>> getExpiringCoupons(@RequestParam LocalDateTime dateTime) {
        List<CouponDTO> expiringCoupons = couponService.getExpiringCoupons(dateTime);
        return ResponseEntity.ok(expiringCoupons);
    }

    @GetMapping
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        List<CouponDTO> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @PostMapping
    public ResponseEntity<Void> saveCoupon(@RequestBody CouponDTO couponDTO) {
        couponService.saveCoupon(couponDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
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



}
