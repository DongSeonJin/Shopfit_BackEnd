package com.spring.shopping.service;

import com.spring.shopping.entity.Wishlist;
import com.spring.shopping.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public Wishlist addItemToWishlist(Wishlist wishlistItem) {
        return wishlistRepository.save(wishlistItem);
    }

    @Override
    public void removeItemFromWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    @Override
    public List<Wishlist> getUserWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    @Override
    public List<Wishlist> getProductWishlist(Long productId) {
        return wishlistRepository.findByProductId(productId);
    }

    @Override
    public int getProductLikeCount(Long productId) {
        return wishlistRepository.countByProductId(productId);
    }
}
