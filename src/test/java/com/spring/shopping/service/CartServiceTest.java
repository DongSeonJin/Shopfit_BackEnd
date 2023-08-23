package com.spring.shopping.service;

import com.spring.shopping.DTO.CartDTO;
import com.spring.shopping.entity.Cart;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.CartRepository;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cartService = new CartServiceImpl(cartRepository, userRepository, productRepository);
    }

    // 장바구니에 추가 테스트 - 성공
    @Test
    public void addToCartTest_Success() {
        // given : 가상의 사용자, 상품 객체 생성 및 설정, fixture 설정
        User user = User.createUser();
        user.setUserId(1L);

        Product product = new Product();
        product.setProductId(2L);

        Long quantity = 3L;

        // userRepository.findById(1L)이 호출될 때 가상 사용자 객체 반환 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // productRepository.findById(2L)이 호출될 때 가상 상품 객체 반환 설정
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        // cartRepository.save(any(Cart.class))가 호출될 때 Cart 객체 반환 설정
        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart());

        // when : 장바구니에 넣기 메서드 호출
        CartDTO cartItem = cartService.addToCart(1L,2L, quantity);

        // then :
        // 생성된 CartDTO 객체가 null이 아님을 검증
        assertNotNull(cartItem);
        // 생성된 CartDTO 객체의 userId가 예상 값과 일치하는지 검증
        assertEquals(1L, cartItem.getUserId());
        // 생성된 CartDTO 객체의 productId가 예상 값과 일치하는지 검증
        assertEquals(2L, cartItem.getProductId());

    }

    // 장바구니에 추가 테스트 - 실패
    @Test
    public void addToCartTest_InvalidUserOrProduct() {
        // given :가상의 사용자, 상품 객체 설정, fixture 설정
        // userRepository.findById(1L)이 호출될 때 빈 Optional 객체 반환 설정
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // productRepository.findById(2L)이 호출될 때 빈 Optional 객체 반환 설정
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Long quantity = 3L;

        // when : 장바구니에 넣기 메서드 호출
        CartDTO cartItem = cartService.addToCart(1L, 2L, quantity);

        // then : 생성된 CartDTO 객체가 null임을 검증
        assertNull(cartItem);
    }

    // 장바구니에서 제거하기 테스트
    @Test
    @ExtendWith(MockitoExtension.class)
    public void removeFromCartTest() {
        // given : 가상의 cartId 설정
        Long cartId = 1L;

        // when : removeFromCart 메서드 호출
        cartService.removeFromCart(cartId);

        // then : cartRepository.deleteById(cartId)가 호출되었는지 검증
        verify(cartRepository, times(1)).deleteById(cartId);
    }


    // 유저Id로 장바구니 목록 가져오기 - 성공
    @Test
    public void getUserCartTest_Success() {
        // given : 가상의 사용자, Cart 객체 생성 및 설정
        User user = User.createUser();
        user.setUserId(1L);

        Cart cartItem = new Cart(user, new Product(), 1L);

        // 생성한 Cart 객체를 담을 리스트 생성
        List<Cart> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        // userRepository.findById(1L)이 호출될 때 가상 사용자 객체 반환 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // cartRepository.findByUser(user)가 호출될 때 생성한 Cart 객체 리스트 반환 설정
        when(cartRepository.findByUser(user)).thenReturn(cartItems);

        // when : 유저 Id로 장바구니 가져오기 메서드 호출
        List<CartDTO> cartDTOList = cartService.getUserCart(1L);

        // then :
        // 생성된 CartDTO 리스트가 null이 아니며 비어있지 않음을 검증
        assertNotNull(cartDTOList);
        assertFalse(cartDTOList.isEmpty());
        // 생성된 CartDTO 리스트의 크기가 1임을 검증
        assertEquals(1, cartDTOList.size());


    }

    // 유저Id로 장바구니 목록 가져오기 - 실패
    @Test
    public void getUserCartTest_InvalidUser() {
        // given : userRepository.findById(1L)이 호출될 때 빈 Optional 객체 반환 설정
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when : 유저 Id로 장바구니 가져오기 메서드 호출
        List<CartDTO> cartDTOList = cartService.getUserCart(1L);

        // then : 생성된 CartDTO 리스트가 null이 아니며 비어있음을 검증
        assertNotNull(cartDTOList); // 빈 Optional을 반환
        assertTrue(cartDTOList.isEmpty());
    }





}
