package com.spring.shopping.controller;

import com.spring.shopping.DTO.CartDTO;
import com.spring.shopping.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartControllerTest {

    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cartController = new CartController(cartService);
    }

    // 장바구니 추가 테스트 - 성공
    @Test
    public void addToCartTest_Success() {
        // given : Mock 데이터를 설정하고 cartItem을 리턴하도록 설정
        CartDTO cartItem = new CartDTO();
        when(cartService.addToCart(1L, 2L, 3L)).thenReturn(cartItem);

        // when : 컨트롤러에서 장바구니에 추가하는 메서드 호출
        ResponseEntity<CartDTO> responseEntity = cartController.addToCart(1L, 2L, 3L);

        // then : Http상태는 created, body는 CartItem
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(cartItem, responseEntity.getBody());
    }

    // 장바구니 추가 테스트 - 실패
    @Test
    public void addToCartTest_InvalidInput() {
        // given : Mock 데이터를 설정하고 addToCart 메서드가 null을 반환하도록 설정
        when(cartService.addToCart(1L, 2L, 3L)).thenReturn(null);

        // when : 컨트롤러의 addToCart 메서드 호출
        ResponseEntity<CartDTO> responseEntity = cartController.addToCart(1L, 2L, 3L);

        // then : Http 상태코드는 BAD_REQUEST, body는 null
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    // 장바구니에서 삭제 테스트
    @Test
    public void removeFromCartTest() {
        // given : 없음

        // when : 컨트롤러의 removeFromCart 메서드 호출
        ResponseEntity<Void> responseEntity = cartController.removeFromCart(1L);

        // Http 상태 코드는 NO_CONTENT, 메서드 호출이 올바르게 되었는지 확인
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(cartService, times(1)).removeFromCart(1L);
    }


    // 유저의 장바구니 목록 가져오기 테스트
    @Test
    public void getUserCartTest() {
        // given : Mock 데이터 설정 및 getUserCart 메서드가 리스트를 반환하도록 설정
        List<CartDTO> cartItems = new ArrayList<>();
        when(cartService.getUserCart(1L)).thenReturn(cartItems);

        // when : 컨트롤러의 getUserCart 메서드 호출
        ResponseEntity<List<CartDTO>> responseEntity = cartController.getUserCart(1L);

        // then : HTTP 상태코드는 OK, body는 cartItems
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cartItems, responseEntity.getBody());
    }

}
