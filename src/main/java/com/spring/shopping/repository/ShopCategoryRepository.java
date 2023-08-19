package com.spring.shopping.repository;

import com.spring.shopping.entity.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopCategoryRepository extends JpaRepository<ShopCategory, Long> {
}
