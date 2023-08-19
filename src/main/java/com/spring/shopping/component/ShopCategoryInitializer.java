package com.spring.shopping.component;

import com.spring.shopping.entity.ShopCategory;
import com.spring.shopping.repository.ShopCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ShopCategoryInitializer implements CommandLineRunner {

    private final ShopCategoryRepository shopCategoryRepository;

    @Autowired
    public ShopCategoryInitializer(ShopCategoryRepository shopCategoryRepository) {
        this.shopCategoryRepository = shopCategoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (shopCategoryRepository.count() == 0) { // 이미 데이터가 있는지 체크
            initializeShopCategories();
        }
    }

    private void initializeShopCategories() {
        ShopCategory chicken = new ShopCategory(1L, "닭가슴살");
        ShopCategory drink = new ShopCategory(2L, "음료/보충제");
        ShopCategory equipment = new ShopCategory(3L, "운동용품");

        shopCategoryRepository.save(chicken );
        shopCategoryRepository.save(drink);
        shopCategoryRepository.save(equipment);
    }
}
