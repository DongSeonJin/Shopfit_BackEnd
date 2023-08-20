package com.spring.shopping.service;

import com.spring.shopping.DTO.ProductDetailResponseDTO;
import com.spring.shopping.DTO.ProductSaveRequestDTO;
import com.spring.shopping.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ProductService {

    // 전체 조회
    Page<Product> getAllProducts(int pageNum);

    // 카테고리별 조회
    public Page<Product> getProductsByCategory(Long categoryId, int pageNum);

    // 상품 상세 조회
    public ProductDetailResponseDTO getProductDetailById(Long productId);

    // 검색
    Page<Product> searchProductsByKeyword(String keyword, int pageNum);

    // 저장
    public boolean saveProductAndImage(ProductSaveRequestDTO requestDTO);

}
