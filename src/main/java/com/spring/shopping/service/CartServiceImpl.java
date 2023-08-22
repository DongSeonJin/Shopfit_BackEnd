package com.spring.shopping.service;

import com.spring.shopping.DTO.CartDTO;
import com.spring.shopping.entity.Cart;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.CartRepository;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    // 장바구니에 추가하기
    @Override
    @Transactional
    public CartDTO addToCart(Long userId, Long productId, Long quantity) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if(userOptional.isPresent() && productOptional.isPresent()) { // userOptional과 productOptional이 존재하면
            User user = userOptional.get(); // 가져오기
            Product product = productOptional.get(); // 가져오기

            Cart cartItem = new Cart(user, product, quantity);
            cartRepository.save(cartItem);

            return convertToDTO(cartItem);

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
    public List<CartDTO> getUserCart(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()) { // user가 존재하면
            User user = userOptional.get(); // 가져오기
            List<CartDTO> cartDTOList = new ArrayList<>(); // CartDTO 리스트를 생성

            List<Cart> cartItems = cartRepository.findByUser(user);  // 사용자의 장바구니에 있는 상품들을 가져오기

            for (Cart cartItem : cartItems) { // // 장바구니에 있는 각 상품을 순회하면서
                cartDTOList.add(convertToDTO(cartItem)); // Cart 엔티티를 CartDTO로 변환하여 리스트에 추가
            }
            return cartDTOList; // 변환된 CartDTO 리스트를 반환
        }
        return new ArrayList<>(); // 사용자가 존재하지 않으면 빈 리스트를 반환
    }


    // 엔터티를 DTO로 변환하기
    private CartDTO convertToDTO(Cart cart) {
        return new CartDTO(cart.getCartId(), cart.getUser().getUserId(), cart.getProduct().getProductId(),
                cart.getQuantity(), cart.getCreatedAt(), cart.getUpdatedAt());
    }


}
