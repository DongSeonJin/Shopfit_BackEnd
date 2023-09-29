package com.spring.shopping.service;

import com.spring.shopping.DTO.ProductDetailResponseDTO;
import com.spring.shopping.DTO.ProductSaveRequestDTO;
import com.spring.shopping.DTO.ProductStockUpdateRequestDTO;
import com.spring.shopping.DTO.ProductUpdateRequestDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.ProductImage;
import com.spring.shopping.entity.ShopCategory;
import com.spring.shopping.repository.ProductImageRepository;
import com.spring.shopping.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    private static final int PAGE_SIZE = 20;

    // 전체 조회 테스트
    @Test
    public void getAllProductsTest() {
        // given: 더미 데이터를 DB에 적재함, fixture 세팅
        int pageNum = 1;

        // when: getAllProducts 호출
        Page<Product> resultPage = productService.getAllProducts(pageNum);

        // then: 결과 페이지는 3개의 아이템을 가져와야 함
        assertThat(resultPage.getContent()).hasSize(3);
    }

    // 정렬 전체 조회 테스트
    @Test
    @Transactional
    public void getAllProductsBySortingTest() {
        // given: 더미 데이터를 DB에 적재함, fixture 세팅
        int pageNum = 1;
        int sortType = 1; // 여기서는 정렬 유형을 예시로 1로 설정

        // when: getAllProductsBySorting 호출
        Page<Product> resultPage = productService.getAllProductsBySorting(pageNum, sortType);

        // then: 결과 페이지의 크기 확인
        assertThat(resultPage.getContent()).hasSize(3);
    }


    // 카테고리 및 정렬 전체 조회 테스트
    @Test
    @Transactional
    public void getProductsByCategoryAndSortingTest() {
        // given: 더미 데이터를 DB에 적재함, fixture 세팅
        int pageNum = 1;
        Long categoryId = 1L;
        int sortType = 1;

        // when: getProductsByCategoryAndSorting 호출
        Page<Product> resultPage = productService.getProductsByCategoryAndSorting(categoryId, pageNum, sortType);

        // then: 결과 페이지의 크기 확인
        assertThat(resultPage.getContent()).hasSize(1);
    }


    // 카테고리별 조회 테스트
    @Test
    @Transactional
    public void getProductsByCategoryTest() {
        // given: 더미 데이터를 DB에 적재함, fixture 세팅
        int pageNum = 1;
        Long categoryId = 1L;

        // when: getProductsByCategory 호출
        Page<Product> resultPage = productService.getProductsByCategory(categoryId, pageNum);

        // then: 결과 페이지는 1개의 아이템을 가져와야 함
        assertThat(resultPage.getContent()).hasSize(1);
    }

    // 상품 1개 상세 조회 테스트
    @Test
    @Transactional
    public void getProductDetailByIdTest() {
        // given: 테스트용 데이터베이스에 존재하는 상품의 ID fixture 세팅
        long productId = 1L;
        
        // when : 상품 id로 조회
        ProductDetailResponseDTO result = productService.getProductDetailById(productId);

        // then: 해당 상품이 예상한 값과 일치하는지 검증
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getProductName()).isEqualTo("찜닭"); // 상품명은 찜닭
        assertThat(result.getProductImageUrls().size()).isEqualTo(2); // 상품 이미지는 2장
    }

    // 상품 검색 테스트
    @Test
    @Transactional
    public void searchProductsByKeywordTest() {
        // given: 테스트용 데이터베이스에 존재하는 상품의 키워드, 페이지번호 fixture 설정
        String keyword = "찜";
        int pageNum = 1;

        // when: searchProductsByKeyword 호출
        Page<Product> searchResults = productService.searchProductsByKeyword(keyword, pageNum);

        // then: 검색 결과는 3개의 상품을 가져와야 함
        assertThat(searchResults.getTotalElements()).isEqualTo(1);
    }

    // 카테고리 내 검색 테스트
    @Test
    @Transactional
    public void searchProductsByCategoryByKeywordTest() {
        // given: 더미 데이터를 DB에 적재함, fixture 세팅
        int pageNum = 1;
        Long categoryId = 1L;
        String keyword = "찜";

        // when: searchProductsByCategoryByKeyword 호출
        Page<Product> resultPage = productService.searchProductsByCategoryByKeyword(categoryId, keyword, pageNum);

        // then: 결과 페이지의 크기 확인
        assertThat(resultPage.getContent()).hasSize(1);
    }


    // 저장 테스트
    @Test
    @Transactional
    public void saveProductAndImageTest() {
        // given : 픽스쳐 설정
        ProductSaveRequestDTO requestDTO = ProductSaveRequestDTO.builder()
                .categoryId(1L)
                .productName("테스트")
                .thumbnailUrl("썸네일 이미지 URL")
                .price(10000L)
                .stockQuantity(50L)
                .productImageUrls(Arrays.asList("이미지 URL 1", "이미지 URL 2", "이미지 URL 3"))
                .build();

        // when : 저장 로직 실행
        boolean success = productService.saveProductAndImage(requestDTO);

        // then : 저장 성공
        assertTrue(success);

    }

    // 삭제 테스트
    @Test
    @Transactional
    public void deleteProductByIdTest() {
        // given : 픽스쳐 설정
        Product product = Product.builder()
                .productId(100L)
                .shopCategory(new ShopCategory())
                .productName("테스트")
                .thumbnailUrl("썸네일 이미지 URL")
                .price(10000L)
                .stockQuantity(50L)
                .productImages(List.of(new ProductImage()))
                .build();

        long productId = product.getProductId();

        // when : 삭제 로직 실행
        productRepository.deleteById(productId);

        // then : productId로 존재하는지 조회 시 false
        assertFalse(productRepository.existsById(productId));
    }



    // 수정 테스트
    @Test
    @Transactional
    public void updateProductTest() {
        // given: 픽스쳐 설정
        ProductSaveRequestDTO requestDTO = ProductSaveRequestDTO.builder()
                .categoryId(1L)
                .productName("테스트")
                .thumbnailUrl("썸네일 이미지 URL")
                .price(10000L)
                .stockQuantity(50L)
                .productImageUrls(Arrays.asList("이미지 URL 1", "이미지 URL 2", "이미지 URL 3"))
                .build();

        boolean saveSuccess = productService.saveProductAndImage(requestDTO);
        assertTrue(saveSuccess, "픽스쳐 데이터 저장 성공");

        // when: 업데이트 로직 실행
        ProductUpdateRequestDTO updateRequestDTO = ProductUpdateRequestDTO.builder()
                .productId(1L)  // 픽스쳐 데이터의 productId를 사용
                .categoryId(2L) // 업데이트할 categoryId
                .productName("수정된 상품명")
                .thumbnailUrl("수정된 썸네일 이미지 URL")
                .price(15000L)
                .stockQuantity(30L)
                .productImageUrls(Arrays.asList("수정된 이미지 URL 1", "수정된 이미지 URL 2"))
                .build();

        boolean updateSuccess = productService.updateProduct(updateRequestDTO);

        // then: 업데이트 성공
        assertTrue(updateSuccess, "업데이트 성공");

        // 테스트에 따라 업데이트 된 값들을 DB에서 조회하여 확인
        Product updatedProduct = productRepository.findById(1L).orElse(null);
        assertNotNull(updatedProduct, "업데이트된 상품 정보를 찾을 수 없음");
        assertEquals(updateRequestDTO.getCategoryId(), updatedProduct.getShopCategory().getCategoryId());
        assertEquals(updateRequestDTO.getProductName(), updatedProduct.getProductName());
        assertEquals(updateRequestDTO.getThumbnailUrl(), updatedProduct.getThumbnailUrl());
        assertEquals(updateRequestDTO.getPrice(), updatedProduct.getPrice());
        assertEquals(updateRequestDTO.getStockQuantity(), updatedProduct.getStockQuantity());

    }

    @Test
    public void getProductInfoTest() {
        // given: 더미 데이터 설정
        Long productId = 1L; // 예시로 사용한 상품 ID

        // when: getProductInfo 호출
        Product resultProduct = productService.getProductInfo(productId);

        // then: 반환된 상품이 적절한지 검증
        assertNotNull(resultProduct);
        assertEquals("찜닭", resultProduct.getProductName());
    }


    @Test
    public void updateProductStockTest() {
        // given: 더미 데이터 설정
        Long productId = 1L;
        Long stockQuantity = 100L;

        ProductStockUpdateRequestDTO requestDTO = new ProductStockUpdateRequestDTO();
        requestDTO.setProductId(productId);
        requestDTO.setStockQuantity(stockQuantity);

        // when: updateProductStock 호출
        boolean result = productService.updateProductStock(requestDTO);

        // then: 반환된 결과 검증
        assertTrue(result);
    }





}


///*Dummy Data input query (TEST DB ONLY)*/
//INSERT INTO product (category_id, product_name, thumbnail_url, price, stock_quantity, created_at, updated_at)
//    VALUES
//            (1, '찜닭', '1.jpg', 500000, 50, NOW(), NOW()),
//        (2, '프로틴', '2.jpg', 20000, 100, NOW(), NOW()),
//        (2, '영양제', '3.jpg', 40000, 75, NOW(), NOW());
//
//        insert into shop_category(category_id, category_name)
//        values
//        (1, "닭가슴살"),
//        (2, "음료/보충제"),
//        (3, "운동용품");
//
//        INSERT INTO product_image (product_id, image_url) VALUES
//        (1, 'product1_image1.jpg'),
//        (1, 'product1_image2.jpg'),
//        (2, 'product2_image1.jpg');
//
//        INSERT INTO review (user_id, product_id, rating, comment, created_at, updated_at)
//        VALUES
//        (1, 1, 5, '맛있어요!', NOW(), NOW()),
//        (2, 1, 4, '괜찮아요', NOW(), NOW()),
//        (3, 2, 5, '좋아요', NOW(), NOW());
//
//        INSERT INTO users (email, password, nickname, point, image_url, created_at, updated_at, is_admin)
//        VALUES
//        ('user1@example.com', 'password1', 'user1', 1000, 'user1.jpg', NOW(), NOW(), false),
//        ('user2@example.com', 'password2', 'user2', 1500, 'user2.jpg', NOW(), NOW(), false),
//        ('user3@example.com', 'password3', 'user3', 5000, 'user3.jpg', NOW(), NOW(), false),
