package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.DTO.OrderProductDTO;
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

    // 주문금액 결제금액 비교
    Long calculateActualOrderAmount(Order order);

    // 배송비; 고정
    Long calculateShippingCost(String address);

    boolean deleteOrder(Long orderId);
}
