package com.spring.shopping.service;

import com.spring.shopping.DTO.ReviewDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.Review;
import com.spring.user.entity.User;
import com.spring.shopping.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ReviewDTO> getReviewsByProduct(Product product) {
        List<Review> reviews = reviewRepository.findByProduct(product);
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(User user) {
        List<Review> reviews = reviewRepository.findByUser(user);
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsWithRatingGreaterThan(Double rating) {
        List<Review> reviews = reviewRepository.findByRatingGreaterThanEqual(rating);
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReviewDTO> getReviewById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.map(this::convertToDTO);
    }

    @Override
    public ReviewDTO createReview(User user, Product product, Double rating, String comment) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());


        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(review.getReviewId());
        reviewDTO.setUserId(review.getUser().getUserId());
        reviewDTO.setProductId(review.getProduct().getProductId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        reviewDTO.setUpdatedAt(review.getUpdatedAt());
        return reviewDTO;
    }
}
