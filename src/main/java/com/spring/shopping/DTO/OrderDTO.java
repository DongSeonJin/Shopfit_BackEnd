package com.spring.shopping.DTO;

import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.OrderProduct;
import com.spring.shopping.entity.Product;
import com.spring.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
    private Long orderId;
    private User user;
    private Long totalPrice;
    private LocalDateTime deliveryDate;
    private String address;
    private String phoneNumber;
    private LocalDateTime orderDate;
    private String orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderProductDTO> orderedProducts;

    public OrderDTO(Order order) {
        this.orderId = order.getOrderId();
        this.user = order.getUser();
        this.totalPrice = order.getTotalPrice();
        this.deliveryDate = order.getDeliveryDate();
        this.address = order.getAddress();
        this.phoneNumber = order.getPhoneNumber();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.orderedProducts = order.getOrderProducts().stream()
                .map(OrderProductDTO::new)
                .collect(Collectors.toList());
    }

    // Getter와 Setter 메서드 생략 (필요한 경우 추가)
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderProductDTO> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<OrderProductDTO> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }


    // OrderProductDTO 내부 클래스
    public static class OrderProductDTO {
        private Long orderProductId;
        private Long orderId; // 주문 ID
        private Long productId; // 상품 ID
        private Long price; // 상품 가격
        private Long quantity; // 주문 수량

        public OrderProductDTO(OrderProduct orderProduct) {
            this.orderProductId = orderProduct.getOrderProductId();
            this.orderId = orderProduct.getOrder().getOrderId();
            this.productId = orderProduct.getProduct().getProductId();
            this.price = orderProduct.getProduct().getPrice();
            this.quantity = orderProduct.getQuantity();
        }

        // Getter와 Setter 메서드
        public Long getOrderProductId() {
            return orderProductId;
        }

        public void setOrderProductId(Long orderProductId) {
            this.orderProductId = orderProductId;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public Product getProductId() {
            return productId;
        }

        public void setProduct(Long productId) {
            this.productId = productId;
        }


        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }

        public Long getQuantity() {
            return quantity;
        }

        public void setQuantity(Long quantity) {
            this.quantity = quantity;
        }
    }
}