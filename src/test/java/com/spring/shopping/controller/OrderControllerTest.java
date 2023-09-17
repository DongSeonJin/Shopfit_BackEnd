package com.spring.shopping.controller;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.service.OrderService;
import com.spring.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testCreateOrder() {
        // OrderDTO 객체 생성
        OrderDTO testOrderDTO = new OrderDTO();
        testOrderDTO.setUserId(1L);
        testOrderDTO.setTotalPrice(100L);

        // orderService.createOrder 모의(mock) 설정: 주문 생성이 성공적으로 이루어짐
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(testOrderDTO);

        // 테스트할 메서드 호출
        ResponseEntity<OrderDTO> response = orderController.createOrder(testOrderDTO);

        // 결과 검증 : 상태코드를 이용하여 객체 생성 여부 확인, 객체 본문이 테스트오더(DTO)와 동일한지 확인
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testOrderDTO, response.getBody());
    }

    @Test
    public void testGetOrdersByUser() {
        // 사용자 생성
        User testUser = User.builder().userId(1L).build();

        // 주문 목록
        List<OrderDTO> testOrders = new ArrayList<>();
        testOrders.add(new OrderDTO());
        testOrders.add(new OrderDTO());

        // orderService.getUserInfo 모의(mock) 설정: 사용자 정보 가져오는 로직
        when(orderService.getUserInfo(testUser.getUserId())).thenReturn(testUser);

        // orderService.getOrdersByUser 모의(mock) 설정: 주문 목록 가져오는 로직
        when(orderService.getOrdersByUser(testUser)).thenReturn(testOrders);

        // 테스트할 메서드 호출
        List<OrderDTO> response = orderController.getOrdersByUser(testUser.getUserId());

        // 결과 검증 : 유저의 주문 수는 2개
        assertNotNull(response);
        assertEquals(testOrders.size(), response.size());
    }

    @Test
    public void testGetOrderById() {
        // 주문 생성
        Order testOrder = Order.builder().orderId(1L).build();

        // 주문 DTO 생성
        OrderDTO testOrderDTO = new OrderDTO();
        testOrderDTO.setOrderId(testOrder.getOrderId());

        // orderService.getOrderInfo 모의(mock) 설정: 주문 정보 가져오는 로직
        when(orderService.getOrderInfo(testOrder.getOrderId())).thenReturn(testOrder);

        // orderService.getOrderById 모의(mock) 설정: 주문 DTO 가져오는 로직
        when(orderService.getOrderById(testOrder.getOrderId())).thenReturn(Optional.of(testOrderDTO));

        // 테스트할 메서드 호출
        Optional<OrderDTO> response = orderController.getOrderById(testOrder.getOrderId());

        // 결과 검증 : 주문번호에 해당하는 오더와 테스트오더(DTO)와 동일
        assertNotNull(response);
        assertTrue(response.isPresent());
        assertEquals(testOrderDTO, response.get());
    }

    @Test
    public void testUpdateOrderStatus() {
        // 주문 ID와 주문 상태 생성
        Long orderId = 1L;
        String orderStatus = "5";       // 5 : 구매확정

        // orderService.updateOrderStatus 모의(mock) 설정: 주문 상태 업데이트 로직
        when(orderService.updateOrderStatus(orderId, orderStatus)).thenReturn(1);

        // 테스트할 메서드 호출
        ResponseEntity<String> response = orderController.updateOrderStatus(orderId, orderStatus);

        // 결과 검증 : 상태코드를 이용하여 객체 업데이트 여부 확인, 알림문이 동일
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("주문 상태가 업데이트되었습니다.", response.getBody());
    }
    
    @Test
    public void testDeleteOrder() {
        // 주문 ID
        Long orderId = 1L;

        // orderService.deleteOrder 모의(mock) 설정: 주문 삭제 로직
        when(orderService.deleteOrder(orderId)).thenReturn(true);

        // 테스트할 메서드 호출
        ResponseEntity<String> response = orderController.deleteOrder(orderId);

        // 결과 검증 : 상태코드를 통해 삭제여부 확인, 삭제알림문이 설정과 같음을 확인
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("주문이 삭제되었습니다.", response.getBody());
    }

}
