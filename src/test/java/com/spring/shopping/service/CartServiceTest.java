package com.spring.shopping.service;

import com.spring.shopping.DTO.CartDTO;
import com.spring.shopping.DTO.CartListResponseDTO;
import com.spring.shopping.entity.Cart;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.CartRepository;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @Transactional
    public void testAddToCart() {
        // 가짜 사용자, 상품, CartDTO 객체 생성
        User testUser = User.builder().userId(1L).build();
        Product testProduct = Product.builder().productId(1L).build();

        CartDTO testCartDTO = new CartDTO();
        testCartDTO.setUserId(testUser.getUserId());
        testCartDTO.setProductId(testProduct.getProductId());
        testCartDTO.setQuantity(2L);

        // userRepository.findById, productRepository.findById 모의(mock) 설정
        when(userRepository.findById(testCartDTO.getUserId())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(testCartDTO.getProductId())).thenReturn(Optional.of(testProduct));

        // 테스트할 메서드 호출
        CartDTO result = cartService.addToCart(testCartDTO);

        // 결과 검증 : 카트에 추가된 상품 정보는 테스트카트(DTO)의 정보와 동일
        assertNotNull(result);
        assertEquals(testCartDTO.getUserId(), result.getUserId());
        assertEquals(testCartDTO.getProductId(), result.getProductId());
        assertEquals(testCartDTO.getQuantity(), result.getQuantity());
    }

    @Test
    @Transactional
    public void testGetUserCart() {
        // 가짜 사용자 객체 생성
        User testUser = User.builder().userId(1L).build();
        Product testProduct1 = Product.builder().productId(1L).build();
        Product testProduct2 = Product.builder().productId(2L).build();

        // userRepository.findById 모의(mock) 설정
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // 가짜 장바구니 아이템 목록 생성
        List<Cart> testCartItems = new ArrayList<>();
        testCartItems.add(new Cart(testUser, testProduct1, 2L));
        testCartItems.add(new Cart(testUser, testProduct2, 3L));

        // cartRepository.findByUser 모의(mock) 설정
        when(cartRepository.findByUser(testUser)).thenReturn(testCartItems);

        // 테스트할 메서드 호출
        List<CartListResponseDTO> result = cartService.getUserCart(testUser.getUserId());

        // 결과 검증 : 테스트 유저의 카트 조회 시, 상품의 종류는 2가지
        assertNotNull(result);
        assertEquals(testCartItems.size(), result.size());
    }

    @Test
    @Transactional
    public void testIsProductInUserCart_WithProductInCart() {
        // 사용자와 상품 생성
        Long userId = 1L;
        Long productId = 1L;

        // cartRepository.findByUserUserIdAndProductProductId 모의(mock) 설정: 장바구니에 해당 상품이 존재함
        when(cartRepository.findByUserUserIdAndProductProductId(userId, productId)).thenReturn(Optional.of(new Cart()));

        // 테스트할 메서드 호출
        boolean result = cartService.isProductInUserCart(userId, productId);

        // 결과 검증 : 유저의 카트에 해당 상품이 존재
        assertTrue(result);
    }

}
