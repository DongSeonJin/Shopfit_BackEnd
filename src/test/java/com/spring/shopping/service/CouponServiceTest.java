package com.spring.shopping.service;

import com.spring.shopping.DTO.CouponDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CouponServiceTest {

    private CouponService couponService;

    @BeforeEach
    void setUp() {
        // 테스트를 위해 CouponServiceImpl 객체를 생성합니다.
        couponService = new CouponServiceImpl();
    }

    @Test
    void testGetCouponById() {
        // 쿠폰을 생성합니다.
        CouponDTO coupon = new CouponDTO();
        coupon.setCouponId(1L);
        coupon.setDescription("Test Coupon");

        // 쿠폰을 저장합니다.
        couponService.saveCoupon(coupon);

        // ID를 사용하여 쿠폰을 조회합니다.
        Optional<CouponDTO> retrievedCoupon = couponService.getCouponById(1L);

        // 존재하는지와 설명이 일치하는지 확인합니다.
        assertTrue(retrievedCoupon.isPresent());
        assertEquals("Test Coupon", retrievedCoupon.get().getDescription());
    }

    @Test
    void testGetCouponsByUserId() {
        // 다른 사용자용 쿠폰을 생성합니다.
        CouponDTO coupon1 = new CouponDTO();
        coupon1.setCouponId(1L);
        coupon1.setUserId(101L);

        // 다른 사용자용 쿠폰을 생성합니다.
        CouponDTO coupon2 = new CouponDTO();
        coupon2.setCouponId(2L);
        coupon2.setUserId(102L);

        // 쿠폰을 저장합니다.
        couponService.saveCoupon(coupon1);
        couponService.saveCoupon(coupon2);

        // 사용자 ID를 사용하여 해당 사용자의 쿠폰을 조회합니다.
        List<CouponDTO> userCoupons = couponService.getCouponsByUserId(101L);

        // 해당 사용자의 쿠폰이 정확히 조회되는지 확인합니다.
        assertEquals(1, userCoupons.size());
        assertEquals(1L, userCoupons.get(0).getCouponId());
    }

    @Test
    void testGetExpiringCoupons() {
        // 다른 만료 날짜를 가진 쿠폰을 생성합니다.
        CouponDTO coupon1 = new CouponDTO();
        coupon1.setCouponId(1L);
        coupon1.setValidTo(LocalDateTime.of(2023, 8, 31, 23, 59));

        // 다른 만료 날짜를 가진 쿠폰을 생성합니다.
        CouponDTO coupon2 = new CouponDTO();
        coupon2.setCouponId(2L);
        coupon2.setValidTo(LocalDateTime.of(2023, 9, 15, 23, 59));

        // 쿠폰을 저장합니다.
        couponService.saveCoupon(coupon1);
        couponService.saveCoupon(coupon2);

        // 지정한 날짜를 기준으로 만료되는 쿠폰을 조회합니다.
        LocalDateTime expiringDateTime = LocalDateTime.of(2023, 9, 1, 0, 0);
        List<CouponDTO> expiringCoupons = couponService.getExpiringCoupons(expiringDateTime);

        // 지정한 날짜보다 이전에 만료되는 쿠폰이 정확히 조회되는지 확인합니다.
        assertEquals(1, expiringCoupons.size());
        assertEquals(1L, expiringCoupons.get(0).getCouponId());
    }
}
