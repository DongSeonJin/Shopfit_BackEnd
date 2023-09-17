package com.spring.shopping.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.shopping.DTO.OrderProductDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.OrderProduct;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.OrderProductRepository;
import com.spring.shopping.repository.OrderRepository;
import com.spring.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderProductServiceImpl(OrderProductRepository orderProductRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderProductRepository = orderProductRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OrderProductDTO> getOrderProductsByOrder(Order order) {
        // 예외처리 추가
        if (order == null) {
            throw new CustomException(ExceptionCode.ORDER_CAN_NOT_BE_NULL);
        }

        List<OrderProduct> orderProducts = orderProductRepository.findByOrder(order);

        if (orderProducts.isEmpty()) {
            throw new CustomException(ExceptionCode.ORDER_PRODUCT_NOT_FOUND);
        }
        return orderProducts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createOrderProduct(Order order, Product product, Long quantity) {
        // 예외처리 추가
        if (order == null) {
            throw new CustomException(ExceptionCode.ORDER_CAN_NOT_BE_NULL);
        }
        if(quantity <= 0) {
            throw new CustomException(ExceptionCode.QUANTITY_INVALID);
        }

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .build();

        orderProductRepository.save(orderProduct);
    }

    @Override
    @Transactional
    public OrderProductDTO addOrderProduct(Order order, Product product, Long quantity) {
        OrderProduct orderProduct = new OrderProduct(order, product, quantity); // OrderProduct 객체 생성
        orderProductRepository.save(orderProduct); // OrderProduct 저장

        return convertToDTO(orderProduct); // OrderProduct를 DTO로 변환하여 리턴
    }

    @Override
    public void removeOrderProduct(Order order, Long productId) {
        OrderProduct orderProduct = orderProductRepository.findByOrderOrderIdAndProductProductId(order, productId);
        if (orderProduct != null) {
            orderProductRepository.delete(orderProduct);
        }
    }

    @Override
    public OrderProductDTO createOrderProduct(OrderProductDTO orderProductDTO) {
        Order order = orderRepository.findById(orderProductDTO.getOrderId()).orElse(null);
        Product product = productRepository.findById(orderProductDTO.getProductId()).orElse(null);
        if (order == null || product == null) {
            // Handle error appropriately (order or product not found)
            return null;
        }
        OrderProduct orderProduct = new OrderProduct(order, product, orderProductDTO.getQuantity());
        orderProduct = orderProductRepository.save(orderProduct);

        orderProductDTO.setOrderProductId(orderProduct.getOrderProductId());
        return orderProductDTO;
    }


    private OrderProductDTO convertToDTO(OrderProduct orderProduct) {
        return OrderProductDTO.builder()
                .orderProductId(orderProduct.getOrderProductId())
                .orderId(orderProduct.getOrder().getOrderId())
                .productId(orderProduct.getProduct().getProductId())
                .quantity(orderProduct.getQuantity())
                .build();
    }
}
