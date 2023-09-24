package com.spring.shopping.service;

import com.spring.exception.BusinessException;
import com.spring.exception.ExceptionCode;
import com.spring.shopping.DTO.OrderDTO;
import com.spring.shopping.DTO.OrderProductDTO;
import com.spring.shopping.DTO.PaymentDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.OrderProduct;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.CouponRepository;
import com.spring.shopping.repository.OrderProductRepository;
import com.spring.shopping.repository.OrderRepository;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
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
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    private final CouponRepository couponRepository;

    // 상품 구매 시 적립되는 포인트 계산을 위한 상수(10%)
    private static final double POINT_PERCENTAGE = 0.10;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                            OrderProductRepository orderProductRepository, ProductRepository productRepository, CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public List<OrderDTO> getOrdersByUser(User user) {
        if (user == null) {
            throw new BusinessException(ExceptionCode.USER_CAN_NOT_BE_NULL);
        }

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
        if (updatedRows <= 0) {
            throw new BusinessException(ExceptionCode.ORDERSTATUS_UPDATE_FAILED);
        }
        return updatedRows;
    }

    // 사용자의 포인트를 업데이트하기
    public void updateUserPoint(Long orderId) {
        //orderId를 이용하여 해당 주문을 가져오기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ORDER_ID_NOT_FOUND));

        User user = order.getUser();
        if (user == null) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }


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
                .build();

        userRepository.save(updatedUser);


    }


    @Override
    public User getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_ID_NOT_FOUND));
    }


    @Override
    public Order getOrderInfo(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ORDER_ID_NOT_FOUND));
    }


    // order + orderProduct 생성
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        // 주문 생성
        Order order = orderRepository.save(Order.builder()
                .user(user)
                .totalPrice(orderDTO.getTotalPrice())
                .deliveryDate(orderDTO.getDeliveryDate())
                .address(orderDTO.getAddress())
                .phoneNumber(orderDTO.getPhoneNumber())
                .orderDate(orderDTO.getOrderDate())
                .orderStatus(orderDTO.getOrderStatus())
                .build());

        Order savedOrder = orderRepository.save(order);

        List<OrderProduct> orderProducts = orderDTO.getOrderProducts().stream()
                .map(orderProductDTO -> convertToOrderProduct(orderProductDTO, savedOrder))
                .collect(Collectors.toList());

        orderProductRepository.saveAll(orderProducts);

        return convertToDTO(savedOrder);
    }


    // 배송비 계산, 고정
    @Override
    public Long calculateShippingCost(String address) {
        return 3000L;                                     // 고정 배송비 3000원
    }

    // 주문금액 검증
    @Override
    public boolean verifyPaymentAmount(PaymentDTO paymentDTO) {
        // 주문 정보를 조회하여 totalPrice를 가져옵니다.
        Optional<Order> orderOptional = orderRepository.findByOrderId(paymentDTO.getOrderId());
        if (orderOptional.isPresent()) {
            Long totalPrice = orderOptional.get().getTotalPrice();
            Long paidAmount = paymentDTO.getPaidAmount();
            Long usingPoint = 0L;
            Long couponDiscount = 0L;
            Long shippingCost = 3000L;

            if (paymentDTO.getUsingPoint() != null) {           // couponId가 null이 아닐 때,
                usingPoint = paymentDTO.getUsingPoint();
            }

            if (paymentDTO.getCouponId() != null) {             // couponId가 null이 아닐 때,
                couponDiscount = couponRepository.findDiscountValueByCouponId(paymentDTO.getCouponId()).orElse(0L);
            }

            return totalPrice + shippingCost == paidAmount + usingPoint + couponDiscount;

        } else {
            // 주문 정보가 없는 경우에는 검증 실패
            return false;
        }
    }


    @Override
    public boolean deleteOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            orderRepository.delete(order);
            return true;
        } else {
            throw new BusinessException(ExceptionCode.ORDER_ID_NOT_FOUND);
        }
    }


    public OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .totalPrice(order.getTotalPrice())
                .deliveryDate(order.getDeliveryDate())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    public OrderProduct convertToOrderProduct(OrderProductDTO dto, Order savedOrder) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_ID_NOT_FOUND));

        return OrderProduct.builder()
                .order(savedOrder)
                .product(product) // productService는 ProductService 클래스로 가정
                .quantity(dto.getQuantity())
                .build();
    }


}