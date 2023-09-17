package com.spring.shopping.DTO;

import com.spring.shopping.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {
    private Long couponId;
    private Long userId;
    private String couponCode;
    private String description;
    private Long discountValue;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    // Coupon 객체를 인자로 받는 생성자 추가
    public CouponDTO(Coupon coupon) {
        this.couponId = coupon.getCouponId();
        this.couponCode = coupon.getCouponCode();
        this.userId = coupon.getUser().getUserId();
        this.description = coupon.getDescription();
        this.discountValue = coupon.getDiscountValue();
        this.validFrom = coupon.getValidFrom();
        this.validTo = coupon.getValidTo();
    }

}
