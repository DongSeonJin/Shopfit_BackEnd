package com.spring.shopping.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.shopping.DTO.CouponDTO;
import com.spring.shopping.entity.Coupon;
import com.spring.shopping.repository.CouponCountRepository;
import com.spring.shopping.repository.CouponRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {
    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponCountRepository couponCountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    @Test
    public void testConfirm() {
        // 가짜 유저 객체 생성
        User testUser = User.builder().userId(1L).build();

        // couponCountRepository.increment 모의(mock) 설정: 발급 가능한 쿠폰 개수를 1로 설정
        when(couponCountRepository.increment()).thenReturn(1L);

        // userRepository.findById 모의(mock) 설정
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // couponRepository.save 모의(mock) 설정: 반환값을 설정하여 void 메서드가 아닌 것처럼 동작하도록 함
        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 테스트할 메서드 호출
        couponService.confirm(testUser.getUserId());

        // 결과 검증
        verify(couponRepository, times(1)).save(any(Coupon.class)); // couponRepository.save 메서드가 한 번 호출되어야 함

        // couponRepository.save 메서드로 전달된 Coupon 객체를 검증
        ArgumentCaptor<Coupon> captor = ArgumentCaptor.forClass(Coupon.class);
        verify(couponRepository).save(captor.capture());

        Coupon savedCoupon = captor.getValue();
        assertNotNull(savedCoupon);
        assertEquals(testUser, savedCoupon.getUser());
        assertEquals("10000COUPON", savedCoupon.getCouponCode());
        assertEquals("10,000원 할인 쿠폰", savedCoupon.getDescription());
        assertEquals(10000L, savedCoupon.getDiscountValue());
        assertNotNull(savedCoupon.getValidFrom());
        assertNotNull(savedCoupon.getValidTo());
    }

    @Test
    public void testConfirmCouponLimitExceeded() {
        // couponCountRepository.increment 모의(mock) 설정: 발급 가능한 쿠폰 개수를 101로 설정
        when(couponCountRepository.increment()).thenReturn(101L);

        // 예외를 기대하는 경우
        assertThrows(CustomException.class, () -> couponService.confirm(1L));
    }

    @Test
    public void testCheckCoupon() {
        // 사용자 ID와 쿠폰 코드 생성
        Long userId = 1L;
        String couponCode = "COUPON123";

        // couponRepository.findByUser_UserIdAndCouponCode 모의(mock) 설정: 쿠폰이 존재하는 경우
        when(couponRepository.findByUser_UserIdAndCouponCode(userId, couponCode)).thenReturn(Optional.of(new Coupon()));

        // 테스트할 메서드 호출
        boolean result = couponService.checkCoupon(userId, couponCode);

        // 결과 검증 : 쿠폰이 존재하는 경우 true를 반환
        assertTrue(result);
    }

    @Test
    public void testCheckCouponNotFound() {
        // 사용자 ID와 쿠폰 코드 생성
        Long userId = 1L;
        String couponCode = "COUPON123";

        // couponRepository.findByUser_UserIdAndCouponCode 모의(mock) 설정: 쿠폰이 존재하지 않는 경우
        when(couponRepository.findByUser_UserIdAndCouponCode(userId, couponCode)).thenReturn(Optional.empty());

        // 테스트할 메서드 호출
        boolean result = couponService.checkCoupon(userId, couponCode);

        // 결과 검증 : 쿠폰이 존재하지 않는 경우 false를 반환
        assertFalse(result);
    }

    @Test
    public void testGetCouponsByUserId() {
        // 가짜 유저 객체 생성
        User testUser = User.builder().userId(1L).build();

        // 가짜 쿠폰 객체 리스트 생성
        Coupon coupon1 = Coupon.builder()
                .user(testUser)
                .couponCode("COUPON1")
                .discountValue(1000L)
                .validFrom(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(30))
                .build();
        Coupon coupon2 = Coupon.builder()
                .user(testUser)
                .couponCode("COUPON2")
                .discountValue(2000L)
                .validFrom(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(15))
                .build();

        // userRepository.findById 모의(mock) 설정
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // couponRepository.findByUser_UserId 모의(mock) 설정: 두 개의 쿠폰을 포함하는 리스트 반환
        when(couponRepository.findByUser_UserId(testUser.getUserId()))
                .thenReturn(List.of(coupon1, coupon2));

        // 테스트할 메서드 호출
        List<CouponDTO> result = couponService.getCouponsByUserId(testUser.getUserId());

        // 결과 검증 : 유효한 쿠폰만 반환되었는지 확인

        LocalDateTime now = LocalDateTime.now();

        assertTrue(result.stream().allMatch(coupon -> coupon.getValidTo().isAfter(now)));
    }

    @Test
    public void testExpireCoupon() {
        // 가짜 쿠폰 객체 생성
        Coupon testCoupon = Coupon.builder()
                .couponId(1L)
                .validTo(LocalDateTime.now().plusDays(1)) // 유효기간을 1일로 설정
                .build();

        // couponRepository.findById 모의(mock) 설정
        when(couponRepository.findById(testCoupon.getCouponId())).thenReturn(Optional.of(testCoupon));

        // 테스트할 메서드 호출
        couponService.expireCoupon(testCoupon.getCouponId());

        // 결과 검증 : 쿠폰의 유효기간이 현재 시간 이전으로 설정되었는지 확인
        assertTrue(testCoupon.getValidTo().isBefore(LocalDateTime.now()));
    }


}
