package com.spring.shopping.controller;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 생성 엔드포인트
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Order createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(new OrderDTO(createdOrder));
    }

    // 모든 주문 목록 조회 엔드포인트
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orderDTOList = orderService.getAllOrders();
        return ResponseEntity.ok(orderDTOList);
    }

    // 주문 아이디로 주문 조회 엔드포인트
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        if (orderDTO == null) {
            return ResponseEntity.notFound().build();                       // 주문이 없을 경우 404 응답 반환
        }
        return ResponseEntity.ok(orderDTO);
    }

    // 주문 취소 엔드포인트
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("주문이 취소되었습니다");                // 주문 취소 성공 메시지 반환
    }

    // 기타 주문과 관련된 엔드포인트 추가 가능
}