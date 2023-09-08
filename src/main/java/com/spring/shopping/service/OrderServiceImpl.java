package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.DTO.OrderProductDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.OrderProduct;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.OrderProductRepository;
import com.spring.shopping.repository.OrderRepository;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    // 상품 구매 시 적립되는 포인트 계산을 위한 상수(10%)
    private static final double POINT_PERCENTAGE = 0.10;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
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

    // 주문 상태 업데이트
    // 주문 상태가 5(구매확정)일 때 사용자의 포인트 업데이트 로직 추가
    @Override
    @Transactional
    public int updateOrderStatus(Long orderId, String orderStatus) {
        int updatedRows = orderRepository.updateOrderStatus(orderId, orderStatus);
        // 주문상태가 5(구매확정)일 때 포인트 업데이트
        if("5".equals(orderStatus)) {
            updateUserPoint(orderId);
        }
        return updatedRows;
    }

    // 사용자의 포인트를 업데이트하기
    private void updateUserPoint(Long orderId) {
        //orderId를 이용하여 해당 주문을 가져오기
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order != null) {
            User user = order.getUser();
            if (user != null) {
                int currentPoint = user.getPoint();
                long additionalPoint =(long) (order.getTotalPrice() * POINT_PERCENTAGE);
                int newPoint = currentPoint + (int) additionalPoint;

                // 빌더 패턴을 사용하여 포인트를 설정한 새로운 User 객체 생성
                User updatedUser = User.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .nickname(user.getNickname())
                        .point(newPoint)
                        .imageUrl(user.getImageUrl())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(LocalDateTime.now())
                        .isAdmin(user.isAdmin())
                        .build();

                userRepository.save(updatedUser);
            }
        }
    }



    @Override
    public User getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }


    @Override
    public Order getOrderInfo(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }


    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = Order.builder()
                .user(user)
                .totalPrice(orderDTO.getTotalPrice())
                .deliveryDate(orderDTO.getDeliveryDate())
                .address(orderDTO.getAddress())
                .phoneNumber(orderDTO.getPhoneNumber())
                .orderDate(orderDTO.getOrderDate())
                .orderStatus(orderDTO.getOrderStatus())
                .build();

        Order savedOrder = orderRepository.save(order);

        // orderProductDTO 데이터 매핑
        List<OrderProductDTO> orderProductDTOs = orderDTO.getOrderProducts().stream()
                .map(orderProduct -> {
                    Product product = productRepository.findById(orderProduct.getProductId())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

                    return OrderProductDTO.builder()
                            .orderId(savedOrder.getOrderId())
                            .productId(product.getProductId())
                            .quantity(orderProduct.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        // saveAll() 쓰기 위해 orderProductDTO -> orderProduct 형태 변환
        List<OrderProduct> orderProducts = orderProductDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        orderProductRepository.saveAll(orderProducts);

        return orderDTO;
    }


    @Override
    public void createOrderProduct(OrderProductDTO orderProductDTO) {
        Order order = orderRepository.findById(orderProductDTO.getOrderId()).orElse(null);
        Product product = productRepository.findById(orderProductDTO.getProductId()).orElse(null);

        if (order != null && product != null) {
            OrderProduct orderProduct = new OrderProduct(order, product, orderProductDTO.getQuantity());
            orderProductRepository.save(orderProduct);
        } else {
            throw new IllegalArgumentException("Invalid order or product information");
        }
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

    public OrderProduct convertToEntity(OrderProductDTO orderProductDTO) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(orderProductDTO.getOrderId());
        orderProduct.setProductId(orderProductDTO.getProductId());
        orderProduct.setQuantity(orderProductDTO.getQuantity());
        return orderProduct;
    }

}