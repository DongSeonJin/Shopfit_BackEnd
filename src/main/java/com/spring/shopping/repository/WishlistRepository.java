package com.spring.shopping.repository;

import com.spring.shopping.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);

    List<Wishlist> findByProductId(Long productId);

    int countByProductId(Long productId);
}
