package com.spring.shopping.service;

import com.spring.shopping.DTO.WishlistDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.Wishlist;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import com.spring.shopping.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Test
    @Transactional
    public void testAddToWishlist() {
        // 가짜 사용자와 상품 객체 생성
        User testUser = User.builder().userId(1L).build();
        Product testProduct = Product.builder().productId(1L).build();

        // userRepository.findById 및 productRepository.findById의 모의(mock) 설정
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(testProduct.getProductId())).thenReturn(Optional.of(testProduct));

        // 위시리스트 아이템을 저장할 때의 모의(mock) 설정
        when(wishlistRepository.save(org.mockito.ArgumentMatchers.any(Wishlist.class))).thenReturn(new Wishlist());

        // 테스트할 메서드 호출
        WishlistDTO result = wishlistService.addToWishlist(testUser.getUserId(), testProduct.getProductId());

        // 결과 검증 : 테스트유저가 추가한 테스트 상품은 찜리스트의 유저, 상품 정보와 일치
        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testProduct.getProductId(), result.getProductId());
    }

    @Test
    @Transactional
    public void testRemoveFromWishlist() {
        // 가짜 위시리스트 아이템 ID
        Wishlist testWishlist = Wishlist.builder().wishlistId(1L).build();

        // 테스트할 메서드 호출
        wishlistService.removeFromWishlist(testWishlist.getWishlistId());

        // 결과 검증 : wishlistRepository.deleteById 메서드가 호출되었는지 검증
        verify(wishlistRepository).deleteById(testWishlist.getWishlistId());
    }

    @Test
    @Transactional
    public void testSelectRemoveFromWishlist() {
        // 사용자와 상품 생성
        User testUser = User.builder().userId(1L).build();
        Product testProduct = Product.builder().productId(1L).build();

        // userRepository.findById 및 productRepository.findById의 모의(mock) 설정
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(testProduct.getProductId())).thenReturn(Optional.of(testProduct));

        // 위시리스트 아이템을 저장할 때의 모의(mock) 설정
        Wishlist testWishlistItem = new Wishlist(testUser, testProduct);
        when(wishlistRepository.findByUserAndProduct(testUser, testProduct)).thenReturn(Optional.of(testWishlistItem));

        // 테스트할 메서드 호출
        WishlistDTO result = wishlistService.removeFromWishlist(testUser.getUserId(), testProduct.getProductId());

        // 결과 검증 : 삭제된 정보는 테스트 정보와 동일
        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testProduct.getProductId(), result.getProductId());
    }

    @Test
    @Transactional
    public void testGetUserWishlist() {
        // 사용자 및 상품 생성
        User testUser = User.builder().userId(1L).build();
        Product testProduct1 = Product.builder().productId(1L).build();
        Product testProduct2 = Product.builder().productId(2L).build();

        // userRepository.findById 모의(mock) 설정
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // 가짜 위시리스트 아이템 목록 생성
        List<Wishlist> testWishlistItems = new ArrayList<>();
        testWishlistItems.add(new Wishlist(testUser, testProduct1));
        testWishlistItems.add(new Wishlist(testUser, testProduct2));

        // wishlistRepository.findByUser 모의(mock) 설정
        when(wishlistRepository.findByUser(testUser)).thenReturn(testWishlistItems);

        // 테스트할 메서드 호출
        List<WishlistDTO> result = wishlistService.getUserWishlist(testUser.getUserId());

        // 결과 검증 : 테스트유저의 찜목록 상품 수는 2개
        assertNotNull(result);
        assertEquals(testWishlistItems.size(), result.size());
    }

    @Test
    @Transactional
    public void testGetProductRowCountMap() {
        // 사용자 및 상품 생성
        User testUser1 = User.builder().userId(1L).build();
        User testUser2 = User.builder().userId(3L).build();
        Product testProduct1 = Product.builder().productId(1L).build();
        Product testProduct2 = Product.builder().productId(2L).build();
        Product testProduct3 = Product.builder().productId(3L).build();
        // 가짜 위시리스트 아이템 목록 생성
        List<Wishlist> testWishlistItems = new ArrayList<>();
        testWishlistItems.add(new Wishlist(testUser1, testProduct1));
        testWishlistItems.add(new Wishlist(testUser1, testProduct2));
        testWishlistItems.add(new Wishlist(testUser2, testProduct1));
        testWishlistItems.add(new Wishlist(testUser2, testProduct3));

        // wishlistRepository.findAll 모의(mock) 설정
        when(wishlistRepository.findAll()).thenReturn(testWishlistItems);

        // 테스트할 메서드 호출
        Map<Long, Long> result = wishlistService.getProductRowCountMap();

        // 결과 검증 : 찜 된 상품 수는 3개, 상품 별 찜 횟수는 각각 2, 1, 1
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(2, result.get(1L));
        assertEquals(1, result.get(2L));
        assertEquals(1, result.get(3L));
    }

}