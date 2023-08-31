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

    // 정렬 전체 조회 - 가격 낮은 순(1), 가격 높은 순(2), 오래된 순(3), 리뷰 많은 순(4)
    Page<Product> getAllProductsBySorting(int pageNum, int sortType);

    // 카테고리 및 정렬 전체 조회
    Page<Product> getProductsByCategoryAndSorting(Long categoryId, int pageNum, int sortType);

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
