package com.spring.shopping.service;

import com.spring.shopping.DTO.ProductDetailResponseDTO;
import com.spring.shopping.DTO.ProductSaveRequestDTO;
import com.spring.shopping.DTO.ProductStockUpdateRequestDTO;
import com.spring.shopping.DTO.ProductUpdateRequestDTO;
import com.spring.shopping.entity.Product;
import org.springframework.data.domain.Page;


public interface ProductService {

    // 전체 조회
    Page<Product> getAllProducts(int pageNum);

    // 카테고리별 조회
    Page<Product> getProductsByCategory(Long categoryId, int pageNum);

    // 상품 상세 조회
    ProductDetailResponseDTO getProductDetailById(Long productId);

    // 검색
    Page<Product> searchProductsByKeyword(String keyword, int pageNum);

    // 카테고리 검색

    Page<Product> searchProductsByCategoryByKeyword(Long categoryId, String keyword, int pageNum);


    //저장
    boolean saveProductAndImage(ProductSaveRequestDTO requestDTO);

    // 삭제
    void deleteProductById(long productId);

    // 수정
    boolean updateProduct(ProductUpdateRequestDTO requestDTO);


    Product getProductInfo(Long productId);

    public boolean updateProductStock(ProductStockUpdateRequestDTO requestDTO);
}
