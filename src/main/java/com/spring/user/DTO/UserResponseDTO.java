package com.spring.user.DTO;

import com.spring.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@Getter
@Setter
public class UserResponseDTO {
    private Long userId;
    private String nickname;
    private int point;
    private String imageUrl;
    private int couponCount; // 쿠폰 개수

    public static UserResponseDTO from(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setNickname(user.getNickname());
        dto.setPoint(user.getPoint());
        dto.setImageUrl(user.getImageUrl());

        // 쿠폰 개수 설정
        // 유효기간이 아직 지나지 않은 쿠폰만 필터링하여 개수 계산
        LocalDateTime currentDate = LocalDateTime.now();
        int couponCount = user.getCoupons() != null ? user.getCoupons()
                .stream()
                .filter(coupon -> coupon.getValidTo().isAfter(currentDate))
                .toList()
                .size()
                : 0;
        dto.setCouponCount(couponCount);

        return dto;
    }

}
