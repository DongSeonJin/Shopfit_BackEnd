package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.repository.OrderRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User testUser;
    private Order testOrder;

    @BeforeEach
    public void setup() {
        testUser = User.createUser();
        testUser.setUserId(1L);

        testOrder = new Order();
        testOrder.setOrderId(1L);
        testOrder.setUser(testUser);
        testOrder.setTotalPrice(100L);
        testOrder.setDeliveryDate(LocalDateTime.now());
        testOrder.setAddress("Test Address");
        testOrder.setPhoneNumber("123-456-7890");
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setOrderStatus("Pending");
    }

    @Test
    void testGetOrdersByUser() {
        // 특정 userId에 해당하는 사용자 정보 조회
        // '엄격성' 적용하여 findById(1L)이 반환하는 스텁 설정  < - >  불필요한 스텁 제거; 아랫줄 제거
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // 특정 사용자의 주문 목록 조회
        // findByUser(testUser)가 반환하는 스텁 설정
        when(orderRepository.findByUser(testUser)).thenReturn(Collections.singletonList(testOrder));

        // 주문 목록을 DTO로 변환하여 가져옴
        List<OrderDTO> orderDTOs = orderService.getOrdersByUser(testUser);

        // 주문 목록의 크기가 1인지 확인
        assertEquals(1, orderDTOs.size());
    }

    @Test
    void testGetOrderById() {
        // 특정 orderId에 해당하는 주문 정보 조회
        // findByOrderId(1L)가 반환하는 스텁 설정
        when(orderRepository.findByOrderId(1L)).thenReturn(Optional.of(testOrder));

        // 주문 정보를 DTO로 변환하여 가져옴
        Optional<OrderDTO> orderDTO = orderService.getOrderById(1L);

        // 주문 정보가 존재하는지 확인
        assertTrue(orderDTO.isPresent());
    }

    @Test
    void testUpdateOrderStatus() {
        // 특정 orderId의 주문의 상태 업데이트
        // updateOrderStatus(1L, "Shipped")가 반환하는 스텁 설정
        when(orderRepository.updateOrderStatus(1L, "Shipped")).thenReturn(1);

        // 주문 상태 업데이트의 결과 값 확인
        int updatedCount = orderService.updateOrderStatus(1L, "Shipped");

        // 업데이트된 주문 개수가 1인지 확인
        assertEquals(1, updatedCount);
    }

}