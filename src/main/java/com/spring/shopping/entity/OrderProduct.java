package com.spring.shopping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long orderProductId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 주문이 삭제되면 주문 물품도 삭제
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 상품이 삭제되면 주문 물품도 삭제
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public OrderProduct(Order order, Product product, Long quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setOrderId(Long orderId) {
        if (this.order == null) {
            this.order = new Order(); // 또는 orderRepository.findById(orderId) 등을 사용하여 실제 Order 객체를 가져와도 됩니다.
        }
        this.order.setOrderId(orderId);
    }

    public void setProductId(Long productId) {
        if (this.product == null) {
            this.product = new Product(); // 또는 productRepository.findById(productId) 등을 사용하여 실제 Product 객체를 가져와도 됩니다.
        }
        this.product.setProductId(productId);
    }
}
