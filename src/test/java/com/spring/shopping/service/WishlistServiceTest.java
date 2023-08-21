package com.spring.shopping.service;

import com.spring.shopping.DTO.WishlistDTO;
import com.spring.shopping.entity.Product;
import com.spring.user.entity.User;
import com.spring.shopping.entity.Wishlist;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.repository.UserRepository;
import com.spring.shopping.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WishlistServiceTest {

    private WishlistService wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        wishlistService = new WishlistServiceImpl(wishlistRepository, userRepository, productRepository);
    }

    @Test
    public void testAddToWishlist_Success() {
        // 가상의 사용자 객체 생성 및 설정
        User user = User.createUser();
        user.setUserId(1L);

        // 가상의 상품 객체 생성 및 설정
        Product product = new Product();
        product.setProductId(2L);

        // userRepository.findById(1L)이 호출될 때 가상 사용자 객체 반환 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // productRepository.findById(2L)이 호출될 때 가상 상품 객체 반환 설정
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        // wishlistRepository.save(any(Wishlist.class))가 호출될 때 Wishlist 객체 반환 설정
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(new Wishlist());

        // addToWishlist 메서드 호출
        WishlistDTO wishlistItem = wishlistService.addToWishlist(1L, 2L);

        // 생성된 WishlistDTO 객체가 null이 아님을 검증
        assertNotNull(wishlistItem);
        // 생성된 WishlistDTO 객체의 userId가 예상 값과 일치하는지 검증
        assertEquals(1L, wishlistItem.getUserId());
        // 생성된 WishlistDTO 객체의 productId가 예상 값과 일치하는지 검증
        assertEquals(2L, wishlistItem.getProductId());
    }

    @Test
    public void testAddToWishlist_InvalidUserOrProduct() {
        // userRepository.findById(1L)이 호출될 때 빈 Optional 객체 반환 설정
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // productRepository.findById(2L)이 호출될 때 빈 Optional 객체 반환 설정
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        // addToWishlist 메서드 호출
        WishlistDTO wishlistItem = wishlistService.addToWishlist(1L, 2L);

        // 생성된 WishlistDTO 객체가 null임을 검증
        assertNull(wishlistItem);
    }

    @Test
    public void testGetUserWishlist_Success() {
        // 가상의 사용자 객체 생성 및 설정
        User user = User.createUser();
        user.setUserId(1L);

        // 가상의 Wishlist 객체 생성
        Wishlist wishlistItem = new Wishlist(user, new Product());
        // 생성한 Wishlist 객체를 담을 리스트 생성
        List<Wishlist> wishlistItems = new ArrayList<>();
        wishlistItems.add(wishlistItem);

        // userRepository.findById(1L)이 호출될 때 가상 사용자 객체 반환 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // wishlistRepository.findByUser(user)가 호출될 때 생성한 Wishlist 객체 리스트 반환 설정
        when(wishlistRepository.findByUser(user)).thenReturn(wishlistItems);

        // getUserWishlist 메서드 호출
        List<WishlistDTO> wishlistDTOList = wishlistService.getUserWishlist(1L);

        // 생성된 WishlistDTO 리스트가 null이 아니며 비어있지 않음을 검증
        assertNotNull(wishlistDTOList);
        assertFalse(wishlistDTOList.isEmpty());
        // 생성된 WishlistDTO 리스트의 크기가 1임을 검증
        assertEquals(1, wishlistDTOList.size());
    }

    @Test
    public void testGetUserWishlist_InvalidUser() {
        // userRepository.findById(1L)이 호출될 때 빈 Optional 객체 반환 설정
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // getUserWishlist 메서드 호출
        List<WishlistDTO> wishlistDTOList = wishlistService.getUserWishlist(1L);

        // 생성된 WishlistDTO 리스트가 null이 아니며 비어있음을 검증
        assertNotNull(wishlistDTOList);
        assertTrue(wishlistDTOList.isEmpty());
    }
}