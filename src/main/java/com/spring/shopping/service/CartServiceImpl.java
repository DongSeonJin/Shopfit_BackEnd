package com.spring.shopping.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.shopping.DTO.CartDTO;
import com.spring.shopping.DTO.CartListResponseDTO;
import com.spring.shopping.entity.Cart;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.CartRepository;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // 장바구니에 추가하기
    @Override
    @Transactional
    public CartDTO addToCart(CartDTO cartDTO) {
        Long userId = cartDTO.getUserId(); // cartDTO에서 userId 가져오기
        Long productId = cartDTO.getProductId(); // cartDTO에서 productId 가져오기
        Long quantity = cartDTO.getQuantity(); // cartDTO에서 quantity 가져오기

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if(userOptional.isPresent() && productOptional.isPresent()) { // userOptional과 productOptional이 존재하면
            User user = userOptional.get(); // 가져오기
            Product product = productOptional.get(); // 가져오기

            Cart cartItem = new Cart(user, product, quantity);
            cartRepository.save(cartItem);
            return convertToDTO(cartItem);
        } else {
            if (!userOptional.isPresent()) {
                throw new CustomException(ExceptionCode.USER_ID_NOT_FOUND);
            } else if (!productOptional.isPresent()) {
                throw new CustomException(ExceptionCode.PRODUCT_ID_NOT_FOUND);
            }
        }
        return null; // 사용자나 상품이 없는 경우
    }

    // 장바구니에서 제거하기
    @Override
    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    // userId로 장바구니 목록 가져오기
    @Override
    public List<CartListResponseDTO> getUserCart(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()) { // user가 존재하면
            User user = userOptional.get(); // 가져오기
            List<CartListResponseDTO> cartDTOList = new ArrayList<>(); // CartListResponseDTO 리스트를 생성

            List<Cart> cartItems = cartRepository.findByUser(user);  // 사용자의 장바구니에 있는 상품들을 가져오기

            for (Cart cartItem : cartItems) { // // 장바구니에 있는 각 상품을 순회하면서
                cartDTOList.add(CartListResponseDTO.from(cartItem)); // Cart 엔티티를 CartListResponseDTO로 변환하여 리스트에 추가
            }
            return cartDTOList; // 변환된 CartDTO 리스트를 반환
        } else {
            throw new CustomException(ExceptionCode.USER_ID_NOT_FOUND); // 사용자가 존재하지 않으면 예외 처리
        }
    }


    // 어떤 유저의 장바구니에 이미 해당 상품이 있는지 확인하기
    public boolean isProductInUserCart(Long userId, Long productId) {
        Optional<Cart> cartOptional = cartRepository.findByUserUserIdAndProductProductId(userId, productId);

        // 장바구니가 존재하고 해당 상품이 포함되어 있는지 여부를 반환합니다.
        return cartOptional.isPresent();
    }

    // 엔터티를 DTO로 변환하기
    private CartDTO convertToDTO(Cart cart) {
        return new CartDTO(cart.getCartId(), cart.getUser().getUserId(), cart.getProduct().getProductId(),
                cart.getQuantity(), cart.getCreatedAt(), cart.getUpdatedAt());
    }


}
