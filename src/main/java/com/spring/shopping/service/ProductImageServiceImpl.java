package com.spring.shopping.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.shopping.entity.ProductImage;
import com.spring.shopping.repository.ProductImageRepository;
import com.spring.shopping.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImpl implements ProductImageService{

    private final ProductImageRepository productImageRepository;

    @Autowired
    public ProductImageServiceImpl(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }



    // 수정 시 프론트단에서 기존 이미지 삭제를 위한 로직
    // 이미지 ID를 기반으로 이미지를 삭제하는 메서드
    @Transactional
    @Override
    public void deleteImageById(Long imageId) {
        // 이미지 ID를 사용하여 데이터베이스에서 해당 이미지를 조회하고 삭제하는 로직
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException(ExceptionCode.IMAGE_ID_NOT_FOUND));

        // 이미지 삭제
        productImageRepository.delete(image);
    }
}
