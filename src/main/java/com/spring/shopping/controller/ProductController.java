package com.spring.shopping.controller;


import com.spring.shopping.DTO.*;
import com.spring.shopping.entity.Product;
import com.spring.shopping.service.ProductImageService;
import com.spring.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


    // 상품 정렬 관련 기능 구현
    // 낮은 가격순 | 높은 가격순 | 신상품순 | 오래된순 | 리뷰 많은순 |
    // 신상품순 - 디폴트정렬이기 때문에 구현할 필요 X

    // 정렬 전체조회 -낮은 가격순(1), 높은 가격순(2), 신상품순(디폴트), 오래된순(3), 리뷰 많은순(4)
    @GetMapping({"/sort/{sortType}/{pageNum}", "/sort/{sortType}"})
    public ResponseEntity<Page<ProductListResponseDTO>> getAllProductsSorted(
            @PathVariable Integer sortType,
            @PathVariable(required = false) Integer pageNum) {
        Page<Product> productPage = productService.getAllProductsBySorting(pageNum, sortType);
        Page<ProductListResponseDTO> productListResponseDTOPage = productPage.map(ProductListResponseDTO::new);
        return ResponseEntity.ok(productListResponseDTOPage);
    }


    // 카테고리별 전체 조회
    @GetMapping({"/category/{categoryId}/{pageNum}", "/category/{categoryId}"})
    public ResponseEntity<Page<ProductListResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PathVariable(required = false) Integer pageNum) {
        int currentPageNum = pageNum != null ? pageNum : 1;
        Page<ProductListResponseDTO> productPage = productService.getProductsByCategory(categoryId, currentPageNum)
                .map(ProductListResponseDTO::new);
        return ResponseEntity.ok(productPage);
    }

    // 카테고리별 조회 및 정렬 - 가격 낮은 순(1), 가격 높은 순(2), 오래된 순(3), 리뷰 많은 순(4)
    @GetMapping({"/category/{categoryId}/sort/{sortType}/{pageNum}", "/category/{categoryId}/sort/{sortType}"})
    public ResponseEntity<Page<ProductListResponseDTO>> getProductsByCategoryAndSorted(
            @PathVariable Long categoryId,
            @PathVariable Integer sortType,
            @PathVariable(required = false) Integer pageNum) {
        Page<Product> productPage = productService.getProductsByCategoryAndSorting(categoryId, pageNum, sortType);
        Page<ProductListResponseDTO> productListResponseDTOPage = productPage.map(ProductListResponseDTO::new);
        return ResponseEntity.ok(productListResponseDTOPage);
    }

        // 개별 상품 상세 조회
        @GetMapping("/detail/{productId}")
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
    @PostMapping
    public ResponseEntity<String> saveProductAndImage(@RequestBody ProductSaveRequestDTO requestDTO) {
        productService.saveProductAndImage(requestDTO);

//        if (success) {
            return ResponseEntity.ok("상품 및 이미지 저장에 성공했습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 및 이미지 저장에 실패했습니다.");
//        } 예외 처리 -> 서비스 레이어에서 처리하도록 수정함
    }


    // ID로 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable long productId){
        productService.deleteProductById(productId);

        return ResponseEntity.ok("상품이 삭제되었습니다");
    }


    // 수정을 위해 기존 데이터 가져오기
    @GetMapping("/updatePage/{productId}")
    public ResponseEntity<ProductUpdateResponseDTO> getUpdateProduct(@PathVariable long productId) {
        Product product = productService.getProductInfo(productId);
//        if(product == null) {
//            throw new ProductIdNotFoundException("없는 상품 번호입니다 : " + productId);
//        } 예외처리 -> 서비스레이어에서 하도록 수정함
        ProductUpdateResponseDTO dto = ProductUpdateResponseDTO.toProductUpdateResponseDTO(product); // 엔터티를 DTO로 변환

        return ResponseEntity.ok(dto);
    }


    // 수정 로직
    @RequestMapping(value="/{productId}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestBody ProductUpdateRequestDTO requestDTO) {

        // json 데이터에 productId를 포함하는 대신 url에 포함시켰으므로 requestBody에 추가해줘야 함
        requestDTO.setProductId(productId);
        productService.updateProduct(requestDTO);

//        if (success) {
            return ResponseEntity.ok("상품 정보 수정에 성공했습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 정보 수정에 실패했습니다.");
//        } 예외 처리를 서비스 레이어에서 하도록 수정함
    }
    
    
    // 상품 수정과 관련하여 프론트에서 기존에 DB에 저장된 사진을 삭제할 때 사용하는 메서드 => 리액트에서 수정 페이지에 버튼으로 만들어야 함
    // productImageId로 해당 이미지를 삭제하기
    @DeleteMapping("/img/{productImageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long productImageId) {

//        if (productImageId != null) {
            productImageService.deleteImageById(productImageId); // 이미지 ID를 기반으로 이미지 삭제
            return ResponseEntity.ok("이미지 삭제 성공");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이미지를 찾을 수 없음");
//        } 예외 처리를 서비스 레이어에서 하도록 수정함

    }


    // 상품의 재고(stock) 수정
    @PostMapping("/stock/{productId}")
    // shopping/stock/{productId}로 수정
    public ResponseEntity<String> updateProductStock(@PathVariable Long productId,
                                                     @RequestBody ProductStockUpdateRequestDTO requestDTO) {
        // json 데이터에 productId를 포함하는 대신 url에 포함시켰으므로 requestBody에 추가해줘야 함
        requestDTO.setProductId(productId);

        productService.updateProductStock(requestDTO);
//        if (success) {
            return ResponseEntity.ok("상품 재고 정보 수정에 성공했습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 재고 정보 수정에 실패했습니다.");
//        } 예외처리를 서비스 레이어에서 하도록 수정함

    }


    // 결제 금액 검증
//        @PostMapping("/verify-payment")
//        public ResponseEntity<String> verifyPaymentAmount(@RequestBody OrderDTO orderDTO) {
//            Long orderId = orderDTO.getOrderId();
//            Long paymentAmount = orderDTO.getTotalPrice();    // 클라이언트에서 제공한 결제 금액
//            System.out.println(paymentAmount);
//
//            // 주문 정보를 DB에서 가져옴
//            Order order = orderService.getOrderInfo(orderId);
//
//            if (order == null) {
//                // 주문을 찾을 수 없음
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
//            }
//
//            // 주문에 저장된 금액
//            Long actualOrderAmount = orderService.calculateActualOrderAmount(order);
//
//            if (paymentAmount.equals(actualOrderAmount)) {
//                // 결제 금액이 실제 주문 금액과 일치하면 주문 생성
//                OrderDTO createdOrder = orderService.createOrder(orderDTO);
//                return ResponseEntity.status(HttpStatus.CREATED).body("주문이 생성되었습니다.");
//            } else {
//                // 결제 금액이 일치하지 않음
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 금액이 일치하지 않습니다.");
//            }
//        }
}
