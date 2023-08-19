package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.OrderProduct;
import com.spring.shopping.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 주문 생성 로직 수행
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        // OrderDTO로부터 필요한 데이터 추출하여 Order 엔터티 생성 및 저장
        Order order = new Order();
        order.setUser(orderDTO.getUser());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setDeliveryDate(orderDTO.getDeliveryDate());
        order.setAddress(orderDTO.getAddress());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setOrderStatus(orderDTO.getOrderStatus());

        // OrderProduct 정보 설정
        List<OrderProduct> orderProducts = orderDTO.getOrderedProducts().stream()
                .map(orderProductDTO -> {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setProductId(orderProductDTO.getProductId());
                    orderProduct.setPrice(orderProductDTO.getPrice());
                    orderProduct.setQuantity(orderProductDTO.getQuantity());
                    return orderProduct;
                })
                .collect(Collectors.toList());

        order.setOrderProducts(orderProducts);

        return orderRepository.save(order); // 생성된 Order 엔터티를 리턴
    }

    // 모든 주문 목록 조회
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    // 주문 아이디로 주문 조회
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            return new OrderDTO(order);
        }
        return null;
    }

    // 주문 취소 로직 수행
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 아이디로 주문 조회
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            // 주문 취소 로직 구현
            // 주문 상태를 변경하거나 기타 관련 작업 수행
            // 예: order.setOrderStatus("CANCELLED");
        }
    }

    // 기타 주문과 관련된 비즈니스 로직 추가 가능
}