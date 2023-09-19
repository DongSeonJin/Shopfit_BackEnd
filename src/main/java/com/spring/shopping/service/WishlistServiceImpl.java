package com.spring.shopping.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.shopping.DTO.WishlistDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.Wishlist;
import com.spring.shopping.repository.ProductRepository;
import com.spring.shopping.repository.WishlistRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_ID_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ExceptionCode.PRODUCT_ID_NOT_FOUND));

        Wishlist wishlistItem = new Wishlist(user, product);
        wishlistRepository.save(wishlistItem);

        return convertToDTO(wishlistItem);
    }

    @Override
    public void removeFromWishlist(Long wishlistId) {
        if(wishlistId == null) {
            throw new CustomException(ExceptionCode.WISHLIST_ID_NOT_FOUND);
        }
        wishlistRepository.deleteById(wishlistId);
    }

    @Override
    public WishlistDTO removeFromWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_ID_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ExceptionCode.PRODUCT_ID_NOT_FOUND));

        Wishlist wishlistItem = wishlistRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new CustomException(ExceptionCode.WISHLIST_NOT_FOUND));

        wishlistRepository.delete(wishlistItem);
        return convertToDTO(wishlistItem);

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

    @Override
    public Map<Long, Long> getProductRowCountMap() {
        List<Wishlist> wishlistItems = wishlistRepository.findAll();

        // 위시리스트에서 각 productId의 등장 횟수를 계산합니다
        return wishlistItems.stream()
                .collect(Collectors.groupingBy(Wishlist::getProductId, Collectors.counting()));

    }


    private WishlistDTO convertToDTO(Wishlist wishlist) {
        return new WishlistDTO(wishlist.getWishlistId(), wishlist.getUser().getUserId(),
                wishlist.getProduct().getProductId(), wishlist.getCreatedAt());
    }
}