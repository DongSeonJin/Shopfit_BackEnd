package com.spring.shopping.controller;

import com.spring.shopping.DTO.CartDTO;
import com.spring.shopping.DTO.CartListResponseDTO;
import com.spring.shopping.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    public void testAddToCart() {
        // 유저, CartDTO 생성
        CartDTO testCartDTO = new CartDTO();
        testCartDTO.setUserId(1L);
        testCartDTO.setProductId(1L);
        testCartDTO.setQuantity(2L);

        // cartService.addToCart 모의(mock) 설정
        when(cartService.addToCart(testCartDTO)).thenReturn(testCartDTO);

        // 테스트할 메서드 호출
        ResponseEntity<CartDTO> response = cartController.addToCart(testCartDTO);

        // 결과 검증 : 장바구니에 추가된 정보와 testCartDTO 동일
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testCartDTO, response.getBody());
    }

    @Test
    public void testRemoveFromCart() {
        // Cart ID 생성
        Long cartId = 1L;

        // cartService.removeFromCart 모의(mock) 설정
        doNothing().when(cartService).removeFromCart(cartId);

        // 테스트할 메서드 호출
        ResponseEntity<Void> response = cartController.removeFromCart(cartId);

        // 결과 검증 : 상태코드를 통해 삭제 여부를 확인하고, 카트 내부는 null
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetUserCart() {
        // 사용자 ID 생성
        Long userId = 1L;

        // 장바구니 아이템 리스트
        List<CartListResponseDTO> testCartItems = new ArrayList<>();
        testCartItems.add(new CartListResponseDTO());
        testCartItems.add(new CartListResponseDTO());

        // cartService.getUserCart 모의(mock) 설정
        when(cartService.getUserCart(userId)).thenReturn(testCartItems);

        // 테스트할 메서드 호출
        ResponseEntity<List<CartListResponseDTO>> response = cartController.getUserCart(userId);

        // 결과 검증 : 유저의 카트에 들어간 정보는 장바구니 아이템 리스트와 동일
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCartItems, response.getBody());
    }
    @Test
    public void testCheckProductInCart() {
        // 사용자 ID와 상품 ID 생성
        Long userId = 1L;
        Long productId = 2L;

        // cartService.isProductInUserCart 모의(mock) 설정: 상품이 장바구니에 있는 경우
        when(cartService.isProductInUserCart(userId, productId)).thenReturn(true);

        // 테스트할 메서드 호출
        ResponseEntity<Boolean> response = cartController.checkProductInCart(userId, productId);

        // 결과 검증 : 장바구니에 상품의 존재여부가 메서드 결과와 동일
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

}
