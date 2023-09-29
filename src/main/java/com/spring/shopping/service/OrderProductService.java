package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderProductDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.Product;

import java.util.List;

public interface OrderProductService {
    List<OrderProductDTO> getOrderProductsByOrder(Order order);

    void createOrderProduct(Order order, Product product, Long quantity);

    OrderProductDTO addOrderProduct(Order order, Product product, Long quantity);

    void removeOrderProduct(Order order, Long productId);
    OrderProductDTO createOrderProduct(OrderProductDTO orderProductDTO);

}