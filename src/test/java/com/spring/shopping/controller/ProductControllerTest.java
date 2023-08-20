package com.spring.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        long productId = 1L;
        String url = "/shopping/products/" + productId;

        // when: 세팅한 경로로 Get 요청 수행 후 결과 저장
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(500000)) // 가격은 500000
                .andExpect(jsonPath("$.reviews",hasSize(2))); // 리뷰 개수는 2개

    }


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
