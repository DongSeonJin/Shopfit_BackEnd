package com.spring.shopping.DTO;

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
}
