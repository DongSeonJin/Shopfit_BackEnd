package com.spring.shopping.repository;

import com.spring.shopping.entity.Wishlist;
import com.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);

    List<Wishlist> findTopProducts(int limit);
}
