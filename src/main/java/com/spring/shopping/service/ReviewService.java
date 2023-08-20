package com.spring.shopping.service;

import com.spring.shopping.DTO.ReviewDTO;
import com.spring.shopping.entity.Product;
import com.spring.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    List<ReviewDTO> getReviewsByProduct(Product product);

    List<ReviewDTO> getReviewsByUser(User user);

    List<ReviewDTO> getReviewsWithRatingGreaterThan(Double rating);

    Optional<ReviewDTO> getReviewById(Long reviewId);

    ReviewDTO createReview(User user, Product product, Double rating, String comment);

    void deleteReview(Long reviewId);

}
