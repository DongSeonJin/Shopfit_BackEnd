package com.spring.shopping.controller;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.service.OrderService;
import com.spring.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 오더 생성
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrdersByUser(@PathVariable Long userId) {
        // 사용자 정보 가져오는 로직
        User user = orderService.getUserInfo(userId);
        return orderService.getOrdersByUser(user);
    }

    @GetMapping("/{orderId}")
    public Optional<OrderDTO> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderInfo(orderId);
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/{orderId}/status")
    public int updateOrderStatus(@PathVariable Long orderId, @RequestParam String orderStatus) {
        return orderService.updateOrderStatus(orderId, orderStatus);
    }

}
