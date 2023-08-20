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
public class OrderDTO {
    private Long orderId;
    private Long userId; // User ID
    private Long totalPrice;
    private LocalDateTime deliveryDate;
    private String address;
    private String phoneNumber;
    private LocalDateTime orderDate;
    private String orderStatus;
}
