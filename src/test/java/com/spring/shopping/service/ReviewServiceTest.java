package com.spring.shopping.service;

import com.spring.shopping.DTO.ReviewDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.Review;
import com.spring.user.entity.User;
import com.spring.shopping.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository; // Mock 객체로 대체된 리뷰 리포지토리

    @InjectMocks
    private ReviewServiceImpl reviewService; // 실제 테스트할 리뷰 서비스

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito의 Mock 및 InjectMocks 어노테이션 초기화
    }

    @Test
    void testGetReviewsByProduct() {
        Product product = new Product(); // 가상의 상품 객체 생성
        when(reviewRepository.findByProduct(product)).thenReturn(new ArrayList<>()); // 리뷰 리포지토리의 findByProduct 메서드 호출 시 빈 리스트 반환 설정

        List<ReviewDTO> result = reviewService.getReviewsByProduct(product); // 리뷰 서비스의 getReviewsByProduct 메서드 호출

        assertEquals(0, result.size()); // 결과가 빈 리스트인지 확인
    }

    @Test
    void testGetReviewsByUser() {
        User user = User.createUser(); // 가상의 사용자 객체 생성
        when(reviewRepository.findByUser(user)).thenReturn(new ArrayList<>()); // 리뷰 리포지토리의 findByUser 메서드 호출 시 빈 리스트 반환 설정

        List<ReviewDTO> result = reviewService.getReviewsByUser(user); // 리뷰 서비스의 getReviewsByUser 메서드 호출

        assertEquals(0, result.size()); // 결과가 빈 리스트인지 확인
    }

    @Test
    void testGetReviewsWithRatingGreaterThan() {
        Double rating = 4.0; // 기준 평점 설정
        when(reviewRepository.findByRatingGreaterThanEqual(rating)).thenReturn(new ArrayList<>()); // 리뷰 리포지토리의 findByRatingGreaterThanEqual 메서드 호출 시 빈 리스트 반환 설정

        List<ReviewDTO> result = reviewService.getReviewsWithRatingGreaterThan(rating); // 리뷰 서비스의 getReviewsWithRatingGreaterThan 메서드 호출

        assertEquals(0, result.size()); // 결과가 빈 리스트인지 확인
    }

    @Test
    void testGetReviewById() {
        Long reviewId = 1L; // 리뷰 ID 설정
        Review review = new Review(); // 가상의 리뷰 객체 생성
        review.setReviewId(reviewId);

        User user = User.createUser(); // 가상의 사용자 객체 생성
        user.setUserId(123L); // 사용자 ID 설정
        review.setUser(user);

        Product product = new Product(); // 가상의 상품 객체 생성
        product.setProductId(456L); // 상품 ID 설정
        review.setProduct(product);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review)); // 리뷰 리포지토리의 findById 메서드 호출 시 가상의 리뷰 객체 반환 설정

        Optional<ReviewDTO> result = reviewService.getReviewById(reviewId); // 리뷰 서비스의 getReviewById 메서드 호출

        assertEquals(reviewId, result.get().getReviewId()); // 결과의 리뷰 ID가 예상 값과 일치하는지 확인
    }

//    @Test
//    @Transactional
//    void testCreateReview() {
//        User user = User.createUser(); // 가상의 사용자 객체 생성
//        user.setUserId(1L); // 사용자 ID 설정
//        Product product = new Product(); // 가상의 상품 객체 생성
//        product.setProductId(4L); // 상품 ID 설정
//        Double rating = 4.5; // 리뷰 평점 설정
//        String comment = "Good product"; // 리뷰 코멘트 설정
//
//        ReviewDTO createdReview = reviewService.createReview(user, product, rating, comment); // 리뷰 서비스의 createReview 메서드 호출
//
//        assertEquals(user.getUserId(), createdReview.getUserId()); // 결과의 사용자 ID가 예상 값과 일치하는지 확인
//        assertEquals(product.getProductId(), createdReview.getProductId()); // 결과의 상품 ID가 예상 값과 일치하는지 확인
//        assertEquals(rating, createdReview.getRating()); // 결과의 평점이 예상 값과 일치하는지 확인
//        assertEquals(comment, createdReview.getComment()); // 결과의 코멘트가 예상 값과 일치하는지 확인
//    }

    @Test
    void testDeleteReview() {
        Long reviewId = 1L; // 리뷰 ID 설정

        reviewService.deleteReview(reviewId); // 리뷰 서비스의 deleteReview 메서드 호출

        verify(reviewRepository, times(1)).deleteById(reviewId); // 리뷰 리포지토리의 deleteById 메서드가 호출되었는지 확인
    }
}
