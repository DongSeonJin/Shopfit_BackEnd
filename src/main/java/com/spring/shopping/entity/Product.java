package com.spring.shopping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
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


    @ManyToOne(fetch = FetchType.LAZY)
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


    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Review> reviews = new ArrayList<>();


    // 데이터베이스에 저장되지 않는 가상의 필드로 선언하기
    @Formula("(SELECT COUNT(r.review_id) FROM review r WHERE r.product_id = product_id)")
    // 리뷰 엔터티(Review)에서 해당 제품(Product)의 리뷰를 찾아서 리뷰 개수를 계산
    private int reviewCount;

}

