package com.spring.shopping.controller;

import com.spring.shopping.DTO.CartDTO;
import com.spring.shopping.DTO.CartListResponseDTO;
import com.spring.shopping.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 장바구니에 추가
    @PostMapping
    public ResponseEntity<CartDTO> addToCart(@RequestBody CartDTO cartDTO) {
        CartDTO cartItem = cartService.addToCart(cartDTO);
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);

    }

    // 장바구니에서 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // userId로 장바구니 목록 가져오기
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartListResponseDTO>> getUserCart(@PathVariable Long userId) {
        List<CartListResponseDTO> cartItems = cartService.getUserCart(userId);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    // 어떤 유저의 장바구니에 이미 해당 상품이 있는지 확인하기
    @GetMapping("/checkCart")
    public ResponseEntity<Boolean> checkProductInCart(@RequestParam Long userId, @RequestParam Long productId) {

        boolean isProductInCart = cartService.isProductInUserCart(userId, productId);

        return ResponseEntity.ok(isProductInCart);
    }

}
