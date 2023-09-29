package com.spring.shopping.service;

import com.spring.shopping.entity.Product;
import com.spring.shopping.entity.ProductImage;
import com.spring.shopping.repository.ProductImageRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ProductImageServiceTest {

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Test
    @Transactional
    public void deleteImageByIdTest() {
        // given: 이미지를 픽스쳐 데이터로 저장하고 이미지 ID 얻기
        ProductImage image = ProductImage.builder()
                .product(new Product())
                .imageUrl("test")
                .build();
        // 이미지 관련 필드 설정
        productImageRepository.save(image);
        Long imageId = image.getProductImageId();

        // when: 이미지 삭제 로직 실행
        assertDoesNotThrow(() -> productImageService.deleteImageById(imageId));
        // assertDoesNotThrow의 인자로 전달되는 람다식은 예외를 발생하지 않는다
        // (예외가 발생하면 테스트가 실패)

        // then: 이미지 삭제 후 조회 시 이미지를 찾을 수 없어야 함
        assertFalse(productImageRepository.existsById(imageId));
    }


}
