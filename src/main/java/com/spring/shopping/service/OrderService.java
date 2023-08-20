package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderDTO;
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
}