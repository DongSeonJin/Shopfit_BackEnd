package com.spring.shopping.repository;

import com.spring.shopping.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 추가적인 쿼리 메서드가 필요하다면 여기에 선언 가능
}