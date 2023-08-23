package com.spring.shopping.service;

import com.spring.shopping.DTO.WishlistDTO;
import com.spring.shopping.entity.Product;
import com.spring.user.entity.User;
import com.spring.shopping.entity.Wishlist;
import com.spring.shopping.repository.ProductRepository;
import com.spring.user.repository.UserRepository;
import com.spring.shopping.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public WishlistDTO addToWishlist(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (userOptional.isPresent() && productOptional.isPresent()) {
            User user = userOptional.get();
            Product product = productOptional.get();

            Wishlist wishlistItem = new Wishlist(user, product);
            wishlistRepository.save(wishlistItem);

            return convertToDTO(wishlistItem);
        }

        return null; // 사용자나 상품이 없을 경우 처리
    }

    @Override
    public void removeFromWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    @Override
    public List<WishlistDTO> getUserWishlist(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<WishlistDTO> wishlistDTOList = new ArrayList<>();

            List<Wishlist> wishlistItems = wishlistRepository.findByUser(user);
            for (Wishlist wishlistItem : wishlistItems) {
                wishlistDTOList.add(convertToDTO(wishlistItem));
            }

            return wishlistDTOList;
        }

        return new ArrayList<>();
    }

    private WishlistDTO convertToDTO(Wishlist wishlist) {
        return new WishlistDTO(wishlist.getWishlistId(), wishlist.getUser().getUserId(),
                wishlist.getProduct().getProductId(), wishlist.getCreatedAt());
    }
}