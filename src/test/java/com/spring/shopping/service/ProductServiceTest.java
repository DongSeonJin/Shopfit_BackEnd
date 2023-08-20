package com.spring.shopping.service;

import com.spring.shopping.DTO.ProductDetailResponseDTO;
import com.spring.shopping.DTO.ProductSaveRequestDTO;
import com.spring.shopping.entity.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    private static final int PAGE_SIZE = 20;

    // 전체 조회 테스트
    @Test
    public void getAllProductsTest() {
        // given: 더미 데이터를 DB에 적재함, fixture 세팅
        int pageNum = 1;

        // when: getAllProducts 호출
        Page<Product> resultPage = productService.getAllProducts(pageNum);

        // then: 결과 페이지는 10개의 아이템을 가져와야 함
        assertThat(resultPage.getContent()).hasSize(10);
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

        // then: 결과 페이지는 4개의 아이템을 가져와야 함
        assertThat(resultPage.getContent()).hasSize(4);
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
        String keyword = "chicken";
        int pageNum = 1;

        // when: searchProductsByKeyword 호출
        Page<Product> searchResults = productService.searchProductsByKeyword(keyword, pageNum);

        // then: 검색 결과는 3개의 상품을 가져와야 함
        assertThat(searchResults.getTotalElements()).isEqualTo(3);
    }

    // 저장 테스트
    @Test
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





}


///*Dummy Data input query (TEST DB ONLY)*/
//INSERT INTO product (category_id, product_name, thumbnail_url, price, stock_quantity, created_at, updated_at)
//    VALUES
//            (1, '찜닭', 'smartphone.jpg', 500000, 50, NOW(), NOW()),
//        (2, '프로틴', 'tshirt.jpg', 20000, 100, NOW(), NOW()),
//        (2, '영양제', 'jeans.jpg', 40000, 75, NOW(), NOW()),
//        (3, '덤벨', 'blender.jpg', 80000, 30, NOW(), NOW()),
//        (1, 'Chicken Breast', 'chicken_breast.jpg', 15000, 100, NOW(), NOW()),
//        (1, 'Grilled Chicken', 'grilled_chicken.jpg', 18000, 80, NOW(), NOW()),
//        (1, 'Chicken Nuggets', 'chicken_nuggets.jpg', 12000, 120, NOW(), NOW()),
//        (2, 'Protein Powder', 'protein_powder.jpg', 30000, 150, NOW(), NOW()),
//        (2, 'Energy Drink', 'energy_drink.jpg', 2500, 200, NOW(), NOW()),
//        (3, 'Dumbbells', 'dumbbells.jpg', 50000, 50, NOW(), NOW());
//
//        INSERT INTO product_image (product_id, image_url) VALUES
//        (1, 'product1_image1.jpg'),
//        (1, 'product1_image2.jpg'),
//        (2, 'product2_image1.jpg'),
//        (3, 'product3_image1.jpg'),
//        (3, 'product3_image2.jpg'),
//        (3, 'product3_image3.jpg'),
//        (4, 'product4_image1.jpg');
//
//        INSERT INTO review (user_id, product_id, rating, comment, created_at, updated_at)
//        VALUES
//        (1, 1, 5, '맛있어요!', NOW(), NOW()),
//        (2, 1, 4, '괜찮아요', NOW(), NOW()),
//        (3, 2, 5, '좋아요', NOW(), NOW()),
//        (4, 3, 3, '별로에요', NOW(), NOW()),
//        (1, 4, 4, '운동하기 좋아요', NOW(), NOW());
//
//        INSERT INTO users (email, password, nickname, point, image_url, created_at, updated_at, is_admin)
//        VALUES
//        ('user1@example.com', 'password1', 'user1', 1000, 'user1.jpg', NOW(), NOW(), false),
//        ('user2@example.com', 'password2', 'user2', 1500, 'user2.jpg', NOW(), NOW(), false),
//        ('user3@example.com', 'password3', 'user3', 5000, 'user3.jpg', NOW(), NOW(), false),
//        ('user4@example.com', 'password4', 'user4', 1000, 'user4.jpg', NOW(), NOW(), false);