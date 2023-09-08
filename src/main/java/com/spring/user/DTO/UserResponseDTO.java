package com.spring.user.DTO;

import com.spring.user.entity.User;
import lombok.Getter;
import lombok.Setter;



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
        int couponCount = user.getCoupons() != null ? user.getCoupons().size() : 0;
        dto.setCouponCount(couponCount);

        return dto;
    }

}
