package com.spring.shopping.service;

import com.spring.shopping.DTO.ReviewDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.Review;
import com.spring.shopping.repository.ProductRepository;
import com.spring.shopping.repository.ReviewRepository;

import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void testGetReviewsByProduct() {
        // 가짜 Review 객체 생성
        User testUser1 = User.builder().userId(1L).build();
        User testUser2 = User.builder().userId(2L).build();
        Product testProduct = Product.builder().productId(1L).build();
        List<Review> testReviews = new ArrayList<>();
        testReviews.add(Review.builder().user(testUser1).product(testProduct).build());
        testReviews.add(Review.builder().user(testUser2).product(testProduct).build());

        // reviewRepository.findByProduct 메서드가 호출될 때 가짜 리뷰 목록을 반환하도록 설정
        when(reviewRepository.findByProduct(testProduct)).thenReturn(testReviews);

        // 테스트할 메서드 호출
        List<ReviewDTO> result = reviewService.getReviewsByProduct(testProduct);

        // 결과 검증 : 테스트제품에 등록 된 리뷰는 2개
        assertEquals(2, result.size());

    }

    @Test
    public void testGetReviewsByUser() {
        // 가짜 User 객체 생성
        User testUser = User.builder().userId(1L).build();
        Product testProduct1 = Product.builder().productId(1L).build();
        Product testProduct2 = Product.builder().productId(2L).build();
        List<Review> testReviews = new ArrayList<>();
        testReviews.add(Review.builder().user(testUser).product(testProduct1).build());
        testReviews.add(Review.builder().user(testUser).product(testProduct1).build());
        testReviews.add(Review.builder().user(testUser).product(testProduct2).build());

        // reviewRepository.findByUser 메서드가 호출될 때 가짜 리뷰 목록을 반환하도록 설정
        when(reviewRepository.findByUser(testUser)).thenReturn(testReviews);

        // 테스트할 메서드 호출
        List<ReviewDTO> result = reviewService.getReviewsByUser(testUser);

        // 결과 검증 : 테스트유저가 등록한 리뷰는 3개
        assertEquals(3, result.size());
    }
    @Test
    public void testDeleteReview() {
        // 테스트용 리뷰 ID
        Review testReview = Review.builder().reviewId(1L).build();

        // 테스트할 메서드 호출
        reviewService.deleteReview(testReview.getReviewId());

        // 결과 검증 : reviewRepository의 deleteById 메서드가 호출되었는지 검증
        verify(reviewRepository).deleteById(testReview.getReviewId());
    }

    @Test
    @Transactional
    public void testCreateReview() {
        // testUser, testProduct, testReviewDTO 생성
        User testUser = User.builder().userId(1L).build();
        Product testProduct = Product.builder().productId(1L).build();
        ReviewDTO testReviewDTO = ReviewDTO.builder()
                .userId(1L)
                .productId(1L)
                .rating(4.5)
                .comment("댓글")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // userRepository.findById 및 productRepository.findById의 모의(mock) 설정
        when(userRepository.findById(testReviewDTO.getUserId())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(testReviewDTO.getProductId())).thenReturn(Optional.of(testProduct));

        // reviewRepository.save의 모의(mock) 설정
        when(reviewRepository.save(org.mockito.ArgumentMatchers.any(Review.class)))
                .thenReturn(Review.builder().user(testUser).product(testProduct).rating(4.5).comment("댓글").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());

        // 테스트할 메서드 호출
        ReviewDTO result = reviewService.createReview(testReviewDTO);

        // 결과 검증 : 생성 된 리뷰와 선언한 리뷰의 요소 동일
        assertNotNull(result);
        assertEquals(testReviewDTO.getUserId(), result.getUserId());
        assertEquals(testReviewDTO.getProductId(), result.getProductId());
        assertEquals(testReviewDTO.getRating(), result.getRating());
        assertEquals(testReviewDTO.getComment(), result.getComment());
    }

}
