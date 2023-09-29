package com.spring.shopping.controller;

import com.spring.shopping.DTO.ReviewDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.service.OrderService;
import com.spring.shopping.service.ProductService;
import com.spring.user.entity.User;
import com.spring.shopping.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public ReviewController(ReviewService reviewService, ProductService productService, OrderService orderService) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.orderService = orderService;
    }

    //특정 상품 리뷰 조회
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProduct(productService.getProductInfo(productId));
        return ResponseEntity.ok(reviews);
    }

    // 특정 유저가 작성한 리뷰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(@PathVariable Long userId) {
        User user = orderService.getUserInfo(userId);
        List<ReviewDTO> reviews = reviewService.getReviewsByUser(user);
        return ResponseEntity.ok(reviews);
    }

    // 특정 리뷰를 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // 리뷰 생성
    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
//        try {
            // ReviewDTO를 사용하여 리뷰를 생성하고 반환
            ReviewDTO createdReview = reviewService.createReview(reviewDTO);
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            // 사용자나 제품을 찾을 수 없는 경우 예외 처리
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } 예외처리 -> 서비스레이어에서 처리하도록 수정함
    }
}
