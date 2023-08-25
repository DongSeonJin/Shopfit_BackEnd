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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

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

    @Override
    @Transactional
    public int updateOrderStatus(Long orderId, String orderStatus) {
        return orderRepository.updateOrderStatus(orderId, orderStatus);
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