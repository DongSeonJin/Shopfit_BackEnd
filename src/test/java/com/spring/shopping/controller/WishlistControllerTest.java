package com.spring.shopping.controller;

import com.spring.shopping.DTO.ProductTop3DTO;
import com.spring.shopping.DTO.WishlistDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.service.ProductService;
import com.spring.shopping.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    @Mock
    private ProductService productService;


    @Test
    public void testAddToWishlist() {
        // userId와 productId, WishlistDTO 생성
        Long testUserId = 1L;
        Long testProductId = 2L;
        WishlistDTO testWishlistItem = new WishlistDTO();

        // wishlistService.addToWishlist 모의(mock) 설정
        when(wishlistService.addToWishlist(testUserId, testProductId)).thenReturn(testWishlistItem);

        // 테스트할 메서드 호출
        ResponseEntity<WishlistDTO> response = wishlistController.addToWishlist(testUserId, testProductId);

        // 결과 검증 : 상태코드를 통해 찜목록에 상품 추가 여부를 확인하고, 찜목록의 내용이 testWishlistItem과 동일
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testWishlistItem, response.getBody());

    }

    @Test
    public void testRemoveFromWishlist() {
        // 가짜 userId와 productId
        Long testUserId = 1L;
        Long testProductId = 2L;

        // 가짜 WishlistDTO
        WishlistDTO testWishlistItem = new WishlistDTO();

        // wishlistService.removeFromWishlist 모의(mock) 설정
        when(wishlistService.removeFromWishlist(testUserId, testProductId)).thenReturn(testWishlistItem);

        // 테스트할 메서드 호출
        ResponseEntity<?> response = wishlistController.removeFromWishlist(testUserId, testProductId);

        // 결과 검증 : 상태코드를 통해 찜목록 상품 삭제 확인, 삭제 후 리스트는 null
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

    }

    @Test
    public void testSelectRemoveFromWishlist() {
        // wishlistId
        Long testWishlistId = 1L;

        // wishlistService.removeFromWishlist 모의(mock) 설정
        doNothing().when(wishlistService).removeFromWishlist(testWishlistId);

        // 테스트할 메서드 호출
        ResponseEntity<Void> response = wishlistController.removeFromWishlist(testWishlistId);

        // 결과 검증 : 상태코드를 통해 삭제 여부 확인
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetUserWishlist() {
        // userId 생성
        Long testUserId = 1L;

        // WishlistDTO 리스트
        List<WishlistDTO> testWishlistItems = new ArrayList<>();
        testWishlistItems.add(new WishlistDTO());
        testWishlistItems.add(new WishlistDTO());

        // wishlistService.getUserWishlist 모의(mock) 설정
        when(wishlistService.getUserWishlist(testUserId)).thenReturn(testWishlistItems);

        // 테스트할 메서드 호출
        ResponseEntity<List<WishlistDTO>> response = wishlistController.getUserWishlist(testUserId);

        // 결과 검증 : 상태코드를 통해 유저 찜목록 호출 확인, 찜목록은 testWishlistItems와 동일
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testWishlistItems, response.getBody());
    }

    @Test
    public void testGetTop3Products_Success() {
        // 상품 생성
        Product testProduct1 = Product.builder().productId(1L).build();
        Product testProduct2 = Product.builder().productId(2L).build();
        Product testProduct3 = Product.builder().productId(3L).build();

        // 상품-찜횟수 맵 생성
        Map<Long, Long> testProductRowCountMap = new HashMap<>();
        testProductRowCountMap.put(1L, 8L);
        testProductRowCountMap.put(2L, 10L);
        testProductRowCountMap.put(3L, 6L);

        // 가짜 상품 목록 생성
        List<Product> testProducts = new ArrayList<>();
        testProducts.add(testProduct1);
        testProducts.add(testProduct2);
        testProducts.add(testProduct3);

        // wishlistService.getProductRowCountMap 모의(mock) 설정
        when(wishlistService.getProductRowCountMap()).thenReturn(testProductRowCountMap);

        // productService.getProductInfo 모의(mock) 설정
        when(productService.getProductInfo(1L)).thenReturn(testProducts.get(0));
        when(productService.getProductInfo(2L)).thenReturn(testProducts.get(1));
        when(productService.getProductInfo(3L)).thenReturn(testProducts.get(2));

        // 테스트할 메서드 호출
        ResponseEntity<List<ProductTop3DTO>> response = wishlistController.getTop3Products();

        // 결과 검증 : 상품마다 찜횟수를 설정하고, 찜횟수에 따라 내림차순으로 정렬된 것을 확인(2 - 1 - 3)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ProductTop3DTO> top3Products = response.getBody();
        assertNotNull(top3Products);
        assertEquals(3, top3Products.size());

        assertEquals(1L, top3Products.get(1).getProductId());
        assertEquals(2L, top3Products.get(0).getProductId());
        assertEquals(3L, top3Products.get(2).getProductId());
    }

}
