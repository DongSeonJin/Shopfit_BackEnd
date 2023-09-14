package com.spring.shopping.controller;

import com.spring.shopping.DTO.ReviewDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.service.OrderService;
import com.spring.shopping.service.ProductService;
import com.spring.shopping.service.ReviewService;
import com.spring.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    public void testGetReviewsByProduct() {
        // 상품 ID
        Long productId = 1L;

        // 리뷰 목록
        List<ReviewDTO> testReviews = new ArrayList<>();
        testReviews.add(new ReviewDTO());
        testReviews.add(new ReviewDTO());

        // productService.getProductInfo 모의(mock) 설정
        when(productService.getProductInfo(productId)).thenReturn(Product.builder().productId(productId).build());

        // reviewService.getReviewsByProduct 모의(mock) 설정
        when(reviewService.getReviewsByProduct(any())).thenReturn(testReviews);

        // 테스트할 메서드 호출
        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByProduct(productId);

        // 결과 검증 : 해당 상품에 등록된 리뷰는 테스트 리뷰와 동일
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testReviews, response.getBody());
    }

    @Test
    public void testGetReviewsByUser() {
        // 가짜 사용자 정보
        User testUser = User.builder().userId(1L).build();

        // 가짜 리뷰 목록
        List<ReviewDTO> testReviews = new ArrayList<>();
        testReviews.add(new ReviewDTO());
        testReviews.add(new ReviewDTO());

        // orderService.getUserInfo 모의(mock) 설정
        when(orderService.getUserInfo(testUser.getUserId())).thenReturn(testUser);

        // reviewService.getReviewsByUser 모의(mock) 설정
        when(reviewService.getReviewsByUser(testUser)).thenReturn(testReviews);

        // 테스트할 메서드 호출
        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByUser(testUser.getUserId());

        // 결과 검증 : 테스트 유저가 작성한 리뷰 목록은 테스트 리뷰 목록과 동일
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testReviews, response.getBody());
    }

    @Test
    public void testDeleteReview() {
        // 리뷰 ID
        Long reviewId = 1L;

        // reviewService.deleteReview 모의(mock) 설정
        doNothing().when(reviewService).deleteReview(reviewId);

        // 테스트할 메서드 호출
        ResponseEntity<Void> response = reviewController.deleteReview(reviewId);

        // 결과 검증 : 상태코드로 삭제 여부를 확인하고, reviewService.deleteReview 메서드가 호출되었는지 검증
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reviewService, times(1)).deleteReview(reviewId);
    }

    @Test
    public void testCreateReview() {
        // ReviewDTO
        ReviewDTO testReviewDTO = new ReviewDTO();

        // reviewService.createReview 모의(mock) 설정
        when(reviewService.createReview(testReviewDTO)).thenReturn(testReviewDTO);

        // 테스트할 메서드 호출
        ResponseEntity<ReviewDTO> response = reviewController.createReview(testReviewDTO);

        // 결과 검증 : 상태코드를 통해 리뷰 작성 여부를 확인하고, 내용이 testReview(DTO)와 동일함을 확인
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testReviewDTO, response.getBody());
    }

}
