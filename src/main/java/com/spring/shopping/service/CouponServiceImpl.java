package com.spring.shopping.service;

import com.spring.shopping.DTO.CouponDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {

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
}
