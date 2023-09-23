package com.spring.shopping.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentDTO {
    private Long orderId;
    private Long paidAmount;
    private Long couponId;
    private Long usingPoint;
}
