package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.DTO.OrderProductDTO;
import com.spring.shopping.DTO.PaymentDTO;
import com.spring.shopping.entity.Order;
import com.spring.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDTO> getOrdersByUser(User user);

    Optional<OrderDTO> getOrderById(Long orderId);

    int updateOrderStatus(Long orderId, String orderStatus);

    User getUserInfo(Long userId);

    Order getOrderInfo(Long orderId);

    OrderDTO createOrder(OrderDTO orderDTO);

    // 배송비; 고정
    Long calculateShippingCost(String address);

    boolean deleteOrder(Long orderId);

    // 주문금액 검증
    boolean verifyPaymentAmount(PaymentDTO paymentDTO);
}
