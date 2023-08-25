package com.spring.shopping.repository;

import com.spring.shopping.entity.Cart;
import com.spring.shopping.entity.Product;
import com.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    Optional<Cart> findByUserUserIdAndProductProductId(Long userId, Long productId);
}
