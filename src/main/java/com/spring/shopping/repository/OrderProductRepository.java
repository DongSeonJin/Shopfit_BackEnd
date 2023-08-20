package com.spring.shopping.repository;

import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.OrderProduct;
import com.spring.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    // 특정 주문에 속한 상품 목록 조회
    List<OrderProduct> findByOrder(Order order);

    // 특정 상품의 주문 내역 조회
    List<OrderProduct> findByProduct(Product product);

    // 주문 ID와 상품 ID를 입력받아 해당 주문 물품을 데이터베이스에서 조회
    OrderProduct findByOrderOrderIdAndProductProductId(Order order, Long productId);     // 수정
}