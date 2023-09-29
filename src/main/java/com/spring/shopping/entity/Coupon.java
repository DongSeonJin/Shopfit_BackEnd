package com.spring.shopping.entity;

import com.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 유저 탈퇴 시 쿠폰도 같이 삭제
    private User user;

    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    @Column(name = "description")
    private String description; // 쿠폰 설명

    @Column(name = "discount_value", nullable = false)
    private Long discountValue; // 할인 가격

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom; // 유효기간 시작날짜

    @Column(name = "valid_to", nullable = false)
    private LocalDateTime validTo; // 유효기간 종료날짜

}
