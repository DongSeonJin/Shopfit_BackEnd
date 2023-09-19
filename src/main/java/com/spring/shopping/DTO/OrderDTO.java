package com.spring.shopping.DTO;

import com.spring.shopping.entity.OrderProduct;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderDTO {
    private Long orderId;
    private Long userId; // User ID
    private Long totalPrice;
    private LocalDateTime deliveryDate;
    private String address;
    private String phoneNumber;
    private LocalDateTime orderDate;
    private String orderStatus;

    private List<OrderProductDTO> orderProducts; // 리스트로 주문 상품들을 관리


}
