package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.OrderProduct;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.OrderProductRepository;
import com.spring.shopping.repository.OrderRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testGetOrdersByUser() {
        // 가짜 유저 생성
        User testUser = User.builder()
                .userId(1L)
                .build();

        // 가짜 주문 데이터 생성
        Order order1 = Order.builder()
                .orderId(1L)
                .user(testUser)
                .build();

        Order order2 = Order.builder()
                .orderId(2L)
                .user(testUser)
                .build();

        // orderRepository.findByUser(mockedUser)가 호출될 때 가짜 주문 데이터 반환하도록 설정
        when(orderRepository.findByUser(testUser)).thenReturn(List.of(order1, order2));

        // 서비스 메서드 호출
        List<OrderDTO> orders = orderService.getOrdersByUser(testUser);

        // 결과 검증 : 테스트유저의 order 수는 2개
        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrderById() {
        // 가짜 유저 생성
        User testUser = User.builder()
                .userId(1L)
                .build();
        // 가짜 주문 데이터 생성
        Order testOrder = Order.builder()
                .orderId(1L)
                .user(testUser)
                .totalPrice(100L)
                .deliveryDate(LocalDateTime.now())
                .address("Test Address")
                .phoneNumber("123-456-7890")
                .orderDate(LocalDateTime.now())
                .orderStatus("2")
                .build();

        // orderRepository.findByOrderId(mockedOrderId)가 호출될 때 가짜 주문 데이터 반환하도록 설정
        when(orderRepository.findByOrderId(testOrder.getOrderId())).thenReturn(Optional.of(testOrder));

        // 서비스 메서드 호출
        Optional<OrderDTO> result = orderService.getOrderById(testOrder.getOrderId());

        // 결과 검증 : 테스트오더는 not null, totalPrice 100
        assertTrue(result.isPresent());
        assertEquals(100, testOrder.getTotalPrice());
    }

    @Test
    void testUpdateOrderStatus() {
        // 가짜 주문 정보 설정
        Long orderId = 1L;
        String orderStatus = "5";

        // orderRepository.updateOrderStatus(mockedOrderId, mockedOrderStatus)가 호출될 때 가짜 업데이트된 행 수 반환하도록 설정
        when(orderRepository.updateOrderStatus(orderId, orderStatus)).thenReturn(1);

        // 서비스 메서드 호출
        int updatedRows = orderService.updateOrderStatus(orderId, orderStatus);

        // 결과 검증 : 업데이트가 완료된 row 수는 1개
        assertEquals(1, updatedRows);
    }

    @Test
    void testUpdateUserPoint() {
        User testUser = User.builder()
                .userId(1L)
                .point(100)
                .build();

        // 가짜 주문 객체 생성
        Order testOrder = Order.builder()
                .orderId(1L)
                .user(testUser)
                .totalPrice(500L) // 주문 가격
                .build();

        // orderRepository.findById(mockedOrderId)가 호출될 때 가짜 주문 반환하도록 설정
        when(orderRepository.findById(testOrder.getOrderId())).thenReturn(Optional.of(testOrder));

        // 새로운 포인트 계산
        double POINT_PERCENTAGE = 0.1; // 포인트 적립율
        long additionalPoint = (long) (testOrder.getTotalPrice() * POINT_PERCENTAGE);
        int newPoint = testUser.getPoint() + (int) additionalPoint;

        // userRepository.save(mockedUser)가 호출될 때 포인트를 업데이트하도록 설정
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            if (args.length == 1 && args[0] instanceof User) {
                User updatedUser = (User) args[0];
                // 결과 검증 : updatedUser point = newPoint = 150; 실제 포인트와 예상 포인트가 동일
                assertEquals(newPoint, updatedUser.getPoint());
            }
            return null;
        }).when(userRepository).save(any(User.class));

        // 서비스 메서드 호출
        orderService.updateUserPoint(testOrder.getOrderId());

    }

    @Test
    void testGetUserInfo() {
        // 가짜 사용자 정보 생성
        User testUser = User.builder()
                .userId(1L)
                .email("test@example.com")
                .password("testpassword")
                .nickname("TestUser")
                .point(100)
                .imageUrl("test.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // userRepository.findById(mockedUserId)가 호출될 때 가짜 사용자 반환하도록 설정
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // 서비스 메서드 호출
        User retrievedUser = orderService.getUserInfo(testUser.getUserId());

        // 결과 검증 : testUser 세팅값 = 호출값
        assertNotNull(retrievedUser);
        assertEquals(testUser.getUserId(), retrievedUser.getUserId());
        assertEquals("test@example.com", retrievedUser.getEmail());
        assertEquals("TestUser", retrievedUser.getNickname());
        assertEquals(100, retrievedUser.getPoint());

        // userRepository.findById(mockedUserId)가 정확히 1번 호출되었는지 확인
        verify(userRepository, times(1)).findById(testUser.getUserId());
    }

    @Test
    void testCreateOrder() {
        // 가짜 유저 생성
        User testUser = User.builder()
                .userId(1L)
                .build();
        Product product1 = Product.builder()
                .productId(1L)
                .build();
        Product product2 = Product.builder()
                .productId(2L)
                .build();

        // 가짜 주문 상품 리스트 생성
        List<OrderProduct> orderProducts = new ArrayList<>();

        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(product1)
                .quantity(2L)
                .build();
        orderProducts.add(orderProduct1);
        OrderProduct orderProduct2 = OrderProduct.builder()
                .product(product2)
                .quantity(5L)
                .build();
        orderProducts.add(orderProduct2);

        // 가짜 OrderDTO 생성
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(testUser.getUserId())
                .totalPrice(5000L)
                .deliveryDate(LocalDateTime.now())
                .address("123 Main St, City")
                .phoneNumber("123-456-7890")
                .orderDate(LocalDateTime.now())
                .orderStatus("2")
                .orderProducts(orderProducts)
                .build();

        // userRepository.findById(mockedUserId)가 호출될 때 가짜 유저 반환하도록 설정
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // 주문 생성 시 반환할 가짜 주문 생성
        Order savedOrder = Order.builder()
                .orderId(1L)
                .user(testUser)
                .totalPrice(orderDTO.getTotalPrice())
                .deliveryDate(orderDTO.getDeliveryDate())
                .address(orderDTO.getAddress())
                .phoneNumber(orderDTO.getPhoneNumber())
                .orderDate(orderDTO.getOrderDate())
                .orderStatus(orderDTO.getOrderStatus())
                .build();

        // orderRepository.save(mockedOrder)가 호출될 때 가짜 주문 반환하도록 설정
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // 서비스 메서드 호출
        OrderDTO createdOrder = orderService.createOrder(orderDTO);

        // 결과 검증 : 테스트에서 생성 된 주문아이디 = 서비스 메서드에서 반환 된 주문아이디
        assertEquals(savedOrder.getOrderId(), createdOrder.getOrderId());
    }

    @Test
    void testDeleteOrder() {
        // 가짜 주문 객체 생성
        Order testOrder = Order.builder()
                .orderId(1L)
                .user(User.builder().userId(1L).build())
                .totalPrice(100L)
                .deliveryDate(LocalDateTime.now())
                .address("Test Address")
                .phoneNumber("123-456-7890")
                .orderDate(LocalDateTime.now())
                .orderStatus("2")
                .build();

        // orderRepository.findById(mockedOrderId)가 호출될 때 가짜 주문 반환하도록 설정
        when(orderRepository.findById(testOrder.getOrderId())).thenReturn(Optional.of(testOrder));

        // 서비스 메서드 호출
        boolean result = orderService.deleteOrder(testOrder.getOrderId());

        // 결과 검증 : 테스트오더 삭제 확인
        assertTrue(result);
    }

}