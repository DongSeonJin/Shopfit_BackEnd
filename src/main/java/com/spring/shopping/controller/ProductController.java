package com.spring.shopping.controller;


import com.spring.shopping.DTO.*;
import com.spring.shopping.entity.Product;
import com.spring.shopping.exception.ProductIdNotFoundException;
import com.spring.shopping.service.ProductImageService;
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
    private final ProductImageService productImageService;

    @Autowired
    public ProductController(ProductService productService, ProductImageService productImageService){
        this.productService = productService;
        this.productImageService = productImageService;
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

    // 카테고리 내 검색
    @GetMapping({"/category/{categoryId}/search/{keyword}/{pageNum}","/category/{categoryId}/search/{keyword}"})
    public ResponseEntity<Page<ProductListResponseDTO>> searchProductsByCategory(@PathVariable Long categoryId,
                                                                                 @PathVariable String keyword,
                                                                                 @PathVariable(required = false) Integer pageNum){
        int currentPageNum = pageNum != null ? pageNum : 1;
        Page<Product> searchResultPage = productService.searchProductsByCategoryByKeyword(categoryId, keyword, currentPageNum);

        Page<ProductListResponseDTO> searchResultDTOPage = searchResultPage
                .map(ProductListResponseDTO::new);
        return ResponseEntity.ok(searchResultDTOPage);
    }

    // 등록
    @PostMapping(value ="/save")
    public ResponseEntity<String> saveProductAndImage(@RequestBody ProductSaveRequestDTO requestDTO) {
        boolean success = productService.saveProductAndImage(requestDTO);

        if (success) {
            return ResponseEntity.ok("상품 및 이미지 저장에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 및 이미지 저장에 실패했습니다.");
        }
    }


    // ID로 삭제
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable long productId){
        productService.deleteProductById(productId);

        return ResponseEntity.ok("상품이 삭제되었습니다");
    }


    // 수정을 위해 기존 데이터 가져오기
    @GetMapping("/update/{productId}")
    public ResponseEntity<ProductUpdateResponseDTO> getUpdateProduct(@PathVariable long productId) {
        Product product = productService.getProductInfo(productId);
        if(product == null) {
            throw new ProductIdNotFoundException("없는 상품 번호입니다 : " + productId);
        }
        ProductUpdateResponseDTO dto = ProductUpdateResponseDTO.toProductUpdateResponseDTO(product); // 엔터티를 DTO로 변환

        return ResponseEntity.ok(dto);
    }


    // 수정 로직
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
    
    
    // 상품 수정과 관련하여 프론트에서 기존에 DB에 저장된 사진을 삭제할 때 사용하는 메서드 => 리액트에서 수정 페이지에 버튼으로 만들어야 함
    // productImageId로 해당 이미지를 삭제하기
    @DeleteMapping("/img/{productImageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long productImageId) {

        try {
            System.out.println(productImageId);
            if (productImageId != null) {
                productImageService.deleteImageById(productImageId); // 이미지 ID를 기반으로 이미지 삭제
                return ResponseEntity.ok("이미지 삭제 성공");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이미지를 찾을 수 없음");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 삭제 실패");
        }

    }

    // 상품의 재고(stock) 수정
    @PostMapping("/update/stock/{productId}")
    public ResponseEntity<String> updateProductStock(@PathVariable Long productId,
                                                     @RequestBody ProductStockUpdateRequestDTO requestDTO) {
        // json 데이터에 productId를 포함하는 대신 url에 포함시켰으므로 requestBody에 추가해줘야 함
        requestDTO.setProductId(productId);
        try {
            boolean success = productService.updateProductStock(requestDTO);
            if (success) {
                return ResponseEntity.ok("상품 재고 정보 수정에 성공했습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 재고 정보 수정에 실패했습니다.");
            }
        } catch (ProductIdNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품을 찾을 수 없음");
        }
    }


}
