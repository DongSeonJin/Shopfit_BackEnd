package com.spring.shopping.service;

import com.spring.shopping.DTO.CouponDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
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


    private final List<CouponDTO> couponList = new ArrayList<>();

    @Override
    public Optional<CouponDTO> getCouponById(Long couponId) {
        return couponList.stream()
                .filter(coupon -> coupon.getCouponId().equals(couponId))
                .findFirst();
    }

    @Override
    public List<CouponDTO> getCouponsByUserId(Long userId) {
        return couponList.stream()
                .filter(coupon -> coupon.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponDTO> getExpiringCoupons(LocalDateTime dateTime) {
        return couponList.stream()
                .filter(coupon -> coupon.getValidTo().isBefore(dateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponDTO> getAllCoupons() {
        return couponList;
    }

    @Override
    public void saveCoupon(CouponDTO couponDTO) {
        couponList.add(couponDTO);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        couponList.removeIf(coupon -> coupon.getCouponId().equals(couponId));
    }


    // 총 쿠폰 발급 개수를 체크하고, 쿠폰을 발급하는 메서드
    @Override
    public void confirm(Long userId) {
        Long count = couponCountRepository.increment(); // 1씩 증가시키고 증가된 값을 받아온다.

        if(count > 100) { // 발급된 쿠폰이 100개가 넘으면 발급 방지
            return;
        }
        User user = getUserById(userId);
        String couponCode = "10000COUPON";  // 쿠폰코드

//        // 이미 해당 사용자에게 해당 쿠폰이 발급되었는지 확인 -- 아래에 메서드로 분리
//        Optional<Coupon> existingCoupon = couponRepository.findByUser_UserIdAndCouponCode(userId, couponCode);
//
//        if(existingCoupon.isPresent()) {
//            // 이미 발급된 쿠폰이 존재하면 중복 발급 방지
//            return;
//        }

        // 중복이 아니라면 쿠폰 발급
        Coupon coupon = createCouponForUser(user, couponCode);
        couponRepository.save(coupon);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // 메인화면의 쿠폰 발급 쿠폰 생성 로직 - 10000원 할인쿠폰
    private Coupon createCouponForUser(User user, String couponCode) {
        return Coupon.builder()
                .user(user)
                .couponCode(couponCode)
                .description("10,000원 할인 쿠폰")
                .discountValue(10000L)
                .validFrom(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(30))
                .build();
    }

    // 어떤 유저가 이미 쿠폰을 가지고 있는지 확인하는 로직
    @Override
    public boolean checkCoupon(Long userId, String couponCode) {
        Optional<Coupon> couponOptional = couponRepository.findByUser_UserIdAndCouponCode(userId, couponCode);

        // 이미 쿠폰이 존재하는지 여부를 반환한다.
        return couponOptional.isPresent();
    }


}
