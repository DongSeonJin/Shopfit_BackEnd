package com.spring.shopping.controller;

import com.spring.shopping.DTO.OrderProductDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.Product;
import com.spring.shopping.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-products")
public class OrderProductController {

    private final OrderProductService orderProductService;

    @Autowired
    public OrderProductController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @GetMapping("/order/{orderId}")
    public List<OrderProductDTO> getOrderProductsByOrder(@PathVariable Order orderId) {
        return orderProductService.getOrderProductsByOrder(orderId);
    }

    @PostMapping("/order/{orderId}/product/{productId}")
    public OrderProductDTO addOrderProduct(
            @PathVariable Order orderId,
            @PathVariable Product productId,
            @RequestParam Long quantity
    ) {
        return orderProductService.addOrderProduct(orderId, productId, quantity);
    }

    @DeleteMapping("/order/{orderId}/product/{productId}")
    public void removeOrderProduct(
            @PathVariable Order orderId,
            @PathVariable Long productId
    ) {
        orderProductService.removeOrderProduct(orderId, productId);
    }

    @PostMapping
    public ResponseEntity<OrderProductDTO> createOrderProduct(@RequestBody OrderProductDTO orderProductDTO) {
        OrderProductDTO createdOrderProduct = orderProductService.createOrderProduct(orderProductDTO);

        if (createdOrderProduct != null) {
            return new ResponseEntity<>(createdOrderProduct, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
