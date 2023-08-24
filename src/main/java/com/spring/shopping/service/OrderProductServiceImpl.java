package com.spring.shopping.service;

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
        List<OrderProduct> orderProducts = orderProductRepository.findByOrder(order);
        return orderProducts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createOrderProduct(Order order, Product product, Long quantity) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setQuantity(quantity);
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
        OrderProductDTO dto = new OrderProductDTO();
        dto.setOrderProductId(orderProduct.getOrderProductId());
        dto.setOrderId(orderProduct.getOrder().getOrderId());
        dto.setProductId(orderProduct.getProduct().getProductId());
        dto.setQuantity(orderProduct.getQuantity());
        return dto;
    }
}
