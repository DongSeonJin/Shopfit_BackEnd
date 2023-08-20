package com.spring.shopping.controller;


import com.spring.shopping.DTO.ProductDetailResponseDTO;
import com.spring.shopping.DTO.ProductListResponseDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }


    // 전체조회
    @GetMapping({"/{pageNum}", ""})
    public ResponseEntity<Page<ProductListResponseDTO>> getAllProducts(
            @PathVariable(required = false) Integer pageNum) {
        int currentPageNum = pageNum != null ? pageNum : 1;
        Page<ProductListResponseDTO> productPage = productService.getAllProducts(currentPageNum)
                .map(ProductListResponseDTO::new); //엔터티를 DTO로 변환하기
        return ResponseEntity.ok(productPage);
    }


    // 카테고리별 조회
    @GetMapping({"/category/{categoryId}/{pageNum}", "/category/{categoryId}"})
    public ResponseEntity<Page<ProductListResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PathVariable(required = false) Integer pageNum) {
        int currentPageNum = pageNum != null ? pageNum : 1;
        Page<ProductListResponseDTO> productPage = productService.getProductsByCategory(categoryId, currentPageNum)
                .map(ProductListResponseDTO::new);
        return ResponseEntity.ok(productPage);
    }

    // 개별 상품 상세 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailResponseDTO> getProductDetail(@PathVariable Long productId) {
        ProductDetailResponseDTO productDetail = productService.getProductDetailById(productId);
        return ResponseEntity.ok(productDetail);
    }


    // 검색
    @GetMapping({"/search/{keyword}/{pageNum}", "/search/{keyword}"})
    public ResponseEntity<Page<ProductListResponseDTO>> searchProducts(@PathVariable String keyword,
                                                                       @PathVariable(required = false) Integer pageNum) {
        int currentPageNum = pageNum != null ? pageNum : 1;
        Page<Product> searchResultsPage = productService.searchProductsByKeyword(keyword, currentPageNum);

        Page<ProductListResponseDTO> searchResultsDTOPage = searchResultsPage
                .map(ProductListResponseDTO::new);

        return ResponseEntity.ok(searchResultsDTOPage);
    }


}
