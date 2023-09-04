package com.spring.shopping.service;

import com.spring.shopping.DTO.ReviewDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.Review;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.entity.User;
import com.spring.shopping.repository.ReviewRepository;
import com.spring.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUserId(review.getUser().getUserId());
        reviewDTO.setProductId(review.getProduct().getProductId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        reviewDTO.setUpdatedAt(review.getUpdatedAt());
        return reviewDTO;
    }

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        // 1. ReviewDTO로부터 필요한 정보 추출
        Long userId = reviewDTO.getUserId();
        Long productId = reviewDTO.getProductId();
        Double rating = reviewDTO.getRating();
        String comment = reviewDTO.getComment();

        // 2. 사용자(User)와 제품(Product) 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다."));

        // 3. 리뷰 생성 및 저장
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        // 4. 저장된 리뷰 정보를 ReviewDTO로 변환하여 반환
        return new ReviewDTO(
                savedReview.getUser().getUserId(),
                savedReview.getProduct().getProductId(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getCreatedAt(),
                savedReview.getUpdatedAt()
        );
    }
}
