package com.spring.shopping.controller;

import com.spring.shopping.DTO.CartDTO;
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
    @PostMapping("/add")
    public ResponseEntity<CartDTO> addToCart(@RequestParam Long userId,
                                             @RequestParam Long productId,
                                             @RequestParam Long quantity) {
        CartDTO cartItem = cartService.addToCart(userId, productId, quantity);
        if(cartItem != null) {
            return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 장바구니에서 삭제
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // userId로 장바구니 목록 가져오기
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartDTO>> getUserCart(@PathVariable Long userId) {
        List<CartDTO> cartItems = cartService.getUserCart(userId);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }


}
