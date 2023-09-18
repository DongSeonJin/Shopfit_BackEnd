package com.spring.shopping.controller;

import com.spring.shopping.DTO.CouponDTO;
import com.spring.shopping.service.CouponService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CouponController couponController;

    @Test
    public void testConfirmCoupon() {
        // 유저 ID 생성
        long userId = 1L;

        // couponService.confirm 모의(mock) 설정
        Mockito.doNothing().when(couponService).confirm(userId);

        // 테스트할 메서드 호출
        ResponseEntity<String> response = couponController.confirmCoupon(userId);

        // 결과 검증 : 응답 상태코드와 메시지 확인
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("쿠폰이 발급되었습니다.", response.getBody());
    }

    @Test
    public void testCheckCoupon() {
        // 사용자 ID와 쿠폰 코드 생성
        Long userId = 1L;
        String couponCode = "COUPON123";

        // couponService.checkCoupon 모의(mock) 설정: 쿠폰이 존재하는 경우
        when(couponService.checkCoupon(userId, couponCode)).thenReturn(true);

        // 테스트할 메서드 호출
        ResponseEntity<Boolean> response = couponController.checkCoupon(userId, couponCode);

        // 결과 검증 : 쿠폰의 존재 여부가 메서드 결과와 동일
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    public void testGetCouponsByUserId() {
        // 사용자 ID 생성
        Long userId = 1L;

        // 쿠폰 리스트 생성
        List<CouponDTO> testCoupons = new ArrayList<>();
        testCoupons.add(new CouponDTO());
        testCoupons.add(new CouponDTO());

        // couponService.getCouponsByUserId 모의(mock) 설정
        when(couponService.getCouponsByUserId(userId)).thenReturn(testCoupons);

        // 테스트할 메서드 호출
        ResponseEntity<List<CouponDTO>> response = couponController.getCouponsByUserId(userId);

        // 결과 검증 : 사용자의 쿠폰 리스트와 메서드 결과가 동일
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCoupons, response.getBody());
    }

    @Test
    public void testExpireCoupon() {
        // 쿠폰 ID 생성
        Long couponId = 1L;

        // couponService.expireCoupon 모의(mock) 설정
        Mockito.doNothing().when(couponService).expireCoupon(couponId);

        // 테스트할 메서드 호출
        couponController.expireCoupon(couponId);

        // 모의(mock) 메서드가 올바르게 호출되었는지 확인
        Mockito.verify(couponService, Mockito.times(1)).expireCoupon(couponId);
    }
}