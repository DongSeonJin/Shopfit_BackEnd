package com.spring.shopping.repository;


import com.spring.shopping.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 전체 조회
    Page<Product> findAll(Pageable pageable);

    // 카테고리별 조회
    Page<Product> findByShopCategoryCategoryId(Long categoryId, Pageable pageable);

    // 상세 조회
    Product findByProductId(Long productId);


    // 검색 - 상품명으로 찾기
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    // 카테고리 내 검색
    Page<Product> findByShopCategoryCategoryIdAndProductNameContainingIgnoreCase(Long categoryId, String keyword, Pageable pageable);

    Product findByProductName(String productName);

    // 비관적 락 - 다른 트랜잭션과의 동시 접근을 허용하지 않고 데이터를 읽거나 수정하기 위해 사용되는 락(잠금) 적용하기
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId = :id")
    Product findByIdPessimistic(@Param("id") Long id);

}
