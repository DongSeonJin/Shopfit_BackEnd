package com.spring.shopping.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.shopping.DTO.CouponDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spring.user.entity.User;
import com.spring.shopping.entity.Coupon;
import com.spring.shopping.repository.CouponCountRepository;
import com.spring.shopping.repository.CouponRepository;
import com.spring.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository; // Redis 사용 레포지토리
    private final UserRepository userRepository;

    @Autowired
    public CouponServiceImpl(CouponRepository couponRepository,
                             CouponCountRepository couponCountRepository,
                             UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository; // 레디스 적용한 Repository
        this.userRepository = userRepository;
    }


    // 총 쿠폰 발급 개수를 체크하고, 쿠폰을 발급하는 메서드
    @Override
    public void confirm(Long userId) {
        Long count = couponCountRepository.increment(); // 1씩 증가시키고 증가된 값을 받아온다.

        if(count > 100) { // 발급된 쿠폰이 100개가 넘으면 발급 방지
            throw new CustomException(ExceptionCode.COUPON_ISSUANCE_EXCEPTION);
        } else { // 발급된 쿠폰이 100개 이하라면 쿠폰 발급

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ExceptionCode.USER_ID_NOT_FOUND));
            String couponCode = "10000COUPON";  // 쿠폰코드

            // 메인화면의 쿠폰 발급 쿠폰 생성 로직 - 10000원 할인쿠폰
            Coupon coupon = Coupon.builder()
                    .user(user)
                    .couponCode(couponCode)
                    .description("10,000원 할인 쿠폰")
                    .discountValue(10000L)
                    .validFrom(LocalDateTime.now())
                    .validTo(LocalDateTime.now().plusDays(30))
                    .build();

            couponRepository.save(coupon);
        }
    }
    @Override
    public void reset() {
        couponCountRepository.reset();
    }



    // 어떤 유저가 이미 쿠폰을 가지고 있는지 확인하는 로직
    @Override
    public boolean checkCoupon(Long userId, String couponCode) {
        Optional<Coupon> couponOptional = couponRepository.findByUser_UserIdAndCouponCode(userId, couponCode);

        // 이미 쿠폰이 존재하는지 여부를 반환한다.
        return couponOptional.isPresent();
    }


    // 유저 ID로 가지고 있는 쿠폰 조회 - (사용 가능한 쿠폰만 조회)
    @Override
    public List<CouponDTO> getCouponsByUserId(Long userId) {
        LocalDateTime currentDate = LocalDateTime.now(); // 현재 날짜 가져오기

        // 주어진 userId에 해당하는 사용자를 찾는다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_ID_NOT_FOUND));

        return couponRepository.findByUser_UserId(userId).stream()
                .filter(coupon -> coupon.getValidTo().isAfter(currentDate)) // 유효기간이 아직 지나지 않은 쿠폰만 선택
                .map(CouponDTO::new) // 엔터티를 DTO로 변환하여 반환
                .collect(Collectors.toList());
    }

    // 쿠폰 사용
    public void expireCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ExceptionCode.CART_ID_NOT_FOUND));

        // 현재 시간으로 유효기간 종료 업데이트
        coupon.setValidTo(LocalDateTime.now());

        couponRepository.save(coupon);
    }

}
