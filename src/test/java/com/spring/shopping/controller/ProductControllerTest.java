package com.spring.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.shopping.DTO.ProductSaveRequestDTO;
import com.spring.shopping.DTO.ProductUpdateRequestDTO;
import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.ProductImage;
import com.spring.shopping.entity.ShopCategory;
import com.spring.shopping.repository.ProductRepository;
import com.spring.shopping.service.ProductService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    // 전체 조회 테스트
    @Test
    public void getAllProductsTest() throws Exception {
        // given: 더미 데이터를 DB에 적재함, url fixture 세팅
        String url = "/shopping/1";

        // when: url로 Get 요청 수행 후 결과를 저장하기
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then: 응답코드 200, 본문 내용은 배열, 그 배열의 길이는 10
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    // 카테고리별 조회 테스트
    @Test
    public void getProductsByCategoryTest() throws Exception {
        // given: 더미 데이터를 DB에 적재함, url fixture 세팅
        String url = "/shopping/category/1";

        // when: url로 Get 요청 수행 후 결과를 저장하기
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then: 응답코드 200, 본문 내용은 배열, 그 배열의 길이는 4
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(4));
    }

    // 상품 1개 상세조회 테스트

    @Test
    public void getProductDetailTest() throws Exception {
        // given: 테스트용 데이터베이스에 존재하는 상품의 ID fixture, 요청 경로 세팅
        long productId = 3L;
        String url = "/shopping/products/" + productId;

        // when: 세팅한 경로로 Get 요청 수행 후 결과 저장
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then: 값 비교
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(40000)) // 가격은 40000
                .andExpect(jsonPath("$.reviews",hasSize(1))); // 리뷰 개수는 1개

    }

    // 검색 테스트
    @Test
    public void searchProductsTest() throws Exception {
        // given: 테스트용 데이터베이스에 존재하는 상품의 키워드, 페이지번호 및 url fixture 설정
        String keyword = "chicken";
        int pageNum = 1;
        String url = "/shopping/search/" + keyword + "/" + pageNum;

        // when: 검색 로직 실행
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then: 응답코드 200, 본문 내용은 배열, 그 배열의 길이는 3개
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    // 저장 테스트
    @Test
    @Transactional
    public void saveProductAndImageTest() throws Exception {
        // given: fixture 설정 및 dto 만들기
        long categoryId = 1;
        String productName = "테스트";
        String thumbnailUrl = "썸네일 이미지 URL";
        long price = 10000;
        long stockQuantity = 50;
        List<String> productImageUrls = new ArrayList<>();
        productImageUrls.add("이미지 URL 1");
        productImageUrls.add("이미지 URL 2");
        productImageUrls.add("이미지 URL 3");

        ProductSaveRequestDTO requestDTO = ProductSaveRequestDTO.builder()
                .categoryId(categoryId)
                .productName(productName)
                .thumbnailUrl(thumbnailUrl)
                .price(price)
                .stockQuantity(stockQuantity)
                .productImageUrls(productImageUrls)
                .build();

        String url = "/shopping/save";

        // 데이터 직렬화
        final String requestBody = objectMapper.writeValueAsString(requestDTO);

        // when : 직렬화된 데이터를 이용해 post 방식으로 url에 요청

        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON) // 전달 자료는 json
                .content(requestBody));  // 위에서 직렬화한 requestBody 변수를 전달

        // then : 응답코드 200
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("상품 및 이미지 저장에 성공했습니다."));

    }

    // 삭제 테스트
    @Test
    @Transactional
    public void deleteProductByIdTest() throws Exception {
        //given : 테스트에 사용할 상품 ID
        long productId = 1L;

        //when: 삭제로직 실행
        ResultActions resultActions = mockMvc.perform(delete("/shopping/delete/" + productId)
                        .contentType(MediaType.APPLICATION_JSON));


        // then: 응답코드 200
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("상품이 삭제되었습니다"));
    }

    // 수정을 위해 기존 데이터 가져오기 테스트
    @Test
    public void testGetUpdateProduct() throws Exception {
        // given
        long productId = 3L;
        String url = "/shopping/update/"+ productId;

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        // then : 응답코드 200
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(40000)); // 가격은 40000
    }

    // 수정 로직 테스트
    @Test
    @Transactional
    public void testUpdateProduct() throws Exception {
        // given : fixture 및 url 세팅
        long productId = 3L;
        String url = "/shopping/update/"+ productId;

        // 업데이트할 상품 정보
        ProductUpdateRequestDTO requestDTO = new ProductUpdateRequestDTO();
        requestDTO.setCategoryId(2L);
        requestDTO.setProductName("Updated Product");
        requestDTO.setThumbnailUrl("http://example.com/thumbnail.jpg");
        requestDTO.setPrice(15000L);
        requestDTO.setStockQuantity(100L);
        requestDTO.setProductImageUrls(Arrays.asList("http://example.com/image1.jpg", "http://example.com/image2.jpg"));

        String requestBody = objectMapper.writeValueAsString(requestDTO);

        // when: PUT 요청으로 상품 업데이트 수행
        ResultActions resultActions = mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // then: 응답코드 200
        resultActions.andExpect(status().isOk());
    }

    // 상품 수정과 관련하여 프론트에서 기존에 DB에 저장된 사진을 삭제할 때 사용하는 메서드 테스트
    // productImageId로 해당 이미지를 삭제하기
    @Test
    @Transactional
    public void testDeleteImage() throws Exception {
        // given : fixture 및 url 세팅
        long productImageId = 2L;
        String url = "/shopping/delete/img/" + productImageId;

        // when: DELETE 요청으로 이미지 삭제 수행
        ResultActions resultActions = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then: 응답코드 200
        resultActions.andExpect(status().isOk());
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
