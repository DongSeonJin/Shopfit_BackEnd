package com.spring.shopping.entity;

import com.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 유저 삭제 시 주문도 삭제
    private User user;

    @Column(name = "total_price")
    private Long totalPrice; // 총 금액

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate; // 배송일

    @Column(name = "address", nullable = false)
    private String address; // 주소

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber; // 핸드폰 번호

    @Column(name = "order_date")
    private LocalDateTime orderDate; // 주문날짜

    @Column(name = "order_status")
    private String orderStatus; // 주문상태

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts;

}