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


}
