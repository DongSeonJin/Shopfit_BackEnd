package com.spring.shopping.controller;


import com.spring.shopping.DTO.*;
import com.spring.shopping.entity.Product;
import com.spring.shopping.exception.ProductIdNotFoundException;
import com.spring.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 등록
    @PostMapping("/save")
    public ResponseEntity<String> saveProductAndImage(@RequestBody ProductSaveRequestDTO requestDTO) {
        boolean success = productService.saveProductAndImage(requestDTO);

        if (success) {
            return ResponseEntity.ok("상품 및 이미지 저장에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 및 이미지 저장에 실패했습니다.");
        }
    }

    // ID로 삭제
    @DeleteMapping("delete/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable long productId){
        productService.deleteProductById(productId);

        return ResponseEntity.ok("상품이 삭제되었습니다");
    }


    // 수정을 위해 기존 데이터 가져오기
    @GetMapping("/update/{productId}")
    public ResponseEntity<ProductUpdateResponseDTO> updateProduct(@PathVariable long productId) {
        Product product = productService.getProductInfo(productId);
        if(product == null) {
            throw new ProductIdNotFoundException("없는 상품 번호입니다 : " + productId);
        }
        ProductUpdateResponseDTO dto = ProductUpdateResponseDTO.toProductUpdateResponseDTO(product); // 엔터티를 DTO로 변환

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value="/update/{productId}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestBody ProductUpdateRequestDTO requestDTO) {

        // json 데이터에 productId를 포함하는 대신 url에 포함시켰으므로 requestBody에 추가해줘야 함
        requestDTO.setProductId(productId);
        boolean success = productService.updateProduct(requestDTO);

//        System.out.println(requestDTO.toString());
        //ProductUpdateRequestDTO{productId=1, categoryId=2, productName='Updated Product', thumbnailUrl='http://example.com/thumbnail.jpg', price=15000, stockQuantity=100, productImageUrls=[http://example.com/image1.jpg, http://example.com/image2.jpg]}

        if (success) {
            return ResponseEntity.ok("상품 정보 수정에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 정보 수정에 실패했습니다.");
        }
    }


}
