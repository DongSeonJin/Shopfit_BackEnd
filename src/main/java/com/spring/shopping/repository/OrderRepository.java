package com.spring.shopping.repository;

import com.spring.shopping.entity.Order;
import com.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주문 목록 조회
    List<Order> findByUser(User user);

    // 개별 주문 조회
    Optional<Order> findByOrderId(Long orderId);

    // 결제 상태 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = ?2 WHERE o.orderId = ?1")
    int updateOrderStatus(Long orderId, String orderStatus);

}