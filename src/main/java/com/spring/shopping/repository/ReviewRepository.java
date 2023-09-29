package com.spring.shopping.repository;

import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.Review;
import com.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    List<Review> findByUser(User user);

    List<Review> findByRatingGreaterThanEqual(Double rating);
}
