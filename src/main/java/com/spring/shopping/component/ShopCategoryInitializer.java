//package com.spring.shopping.component;
//
//import com.spring.shopping.entity.ShopCategory;
//import com.spring.shopping.repository.ShopCategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//// CommandLineRunner 인터페이스를 구현하면 Spring Boot 애플리케이션을 실행할 때, run 메서드 내의 코드가 실행됨
//// shopCategory에 정해진 데이터를 넣는 로직이 애플리케이션 실행 시 자동으로 실행되도록 설정하기 위해 작성함
//public class ShopCategoryInitializer implements CommandLineRunner {
//
//    // DB 접근을 위한 멤버변수
//    private final ShopCategoryRepository shopCategoryRepository;
//
//    @Autowired
//    public ShopCategoryInitializer(ShopCategoryRepository shopCategoryRepository) {
//        this.shopCategoryRepository = shopCategoryRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 상점 카테고리가 DB에 없는지 확인
//        if (shopCategoryRepository.count() == 0) {
//            // 카테고리가 없으면 초기화를 진행
//            initializeShopCategories();
//        }
//    }
//
//    private void initializeShopCategories() {
//        // 카테고리 3가지(닭가슴살, 음료/보충제, 운동용품의 인스턴스 생성)
//        ShopCategory chicken = new ShopCategory(1L, "닭가슴살");
//        ShopCategory drink = new ShopCategory(2L, "음료/보충제");
//        ShopCategory equipment = new ShopCategory(3L, "운동용품");
//        // 상점 카테고리를 DB에 저장
//        shopCategoryRepository.save(chicken );
//        shopCategoryRepository.save(drink);
//        shopCategoryRepository.save(equipment);
//    }
//}
