package com.spring.shopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = {"productImages", "reviews"}) //상호 참조로 인한 무한 재귀 호출 방지
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private ShopCategory shopCategory;

    @Column(name = "product_name", nullable = false)
    private String productName;


    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
  

    @Column(name = "price", nullable = false)
    private Long price;


    @Column(name = "stock_quantity", nullable = false)
    private Long stockQuantity; // 재고수량

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true) // product 삭제 시 productImage도 삭제
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true) // product 삭제 시 해당 review도 삭제
    private List<Review> reviews = new ArrayList<>();


}

