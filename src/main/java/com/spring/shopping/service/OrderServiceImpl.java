package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.repository.OrderRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<OrderDTO> getOrdersByUser(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findByOrderId(orderId);
        return order.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public int updateOrderStatus(Long orderId, String orderStatus) {
        return orderRepository.updateOrderStatus(orderId, orderStatus);
    }

    @Override
    public User getUserInfo(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Order getOrderInfo(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUser().getUserId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setDeliveryDate(order.getDeliveryDate());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setPhoneNumber(order.getPhoneNumber());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setOrderStatus(order.getOrderStatus());
        return orderDTO;
    }
}