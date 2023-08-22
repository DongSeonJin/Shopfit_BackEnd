INSERT INTO product (category_id, product_name, thumbnail_url, price, stock_quantity, created_at, updated_at)
VALUES
    (1, '찜닭', 'smartphone.jpg', 500000, 50, NOW(), NOW()),
    (2, '프로틴', 'tshirt.jpg', 20000, 100, NOW(), NOW()),
    (2, '영양제', 'jeans.jpg', 40000, 75, NOW(), NOW()),
    (3, '덤벨', 'blender.jpg', 80000, 30, NOW(), NOW()),
    (1, 'Chicken Breast', 'chicken_breast.jpg', 15000, 100, NOW(), NOW()),
    (1, 'Grilled Chicken', 'grilled_chicken.jpg', 18000, 80, NOW(), NOW()),
    (1, 'Chicken Nuggets', 'chicken_nuggets.jpg', 12000, 120, NOW(), NOW()),
    (2, 'Protein Powder', 'protein_powder.jpg', 30000, 150, NOW(), NOW()),
    (2, 'Energy Drink', 'energy_drink.jpg', 2500, 200, NOW(), NOW()),
    (3, 'Dumbbells', 'dumbbells.jpg', 50000, 50, NOW(), NOW());

INSERT INTO product_image (product_id, image_url) VALUES
                                                      (1, 'product1_image1.jpg'),
                                                      (1, 'product1_image2.jpg'),
                                                      (2, 'product2_image1.jpg'),
                                                      (3, 'product3_image1.jpg'),
                                                      (3, 'product3_image2.jpg'),
                                                      (3, 'product3_image3.jpg'),
                                                      (4, 'product4_image1.jpg');

INSERT INTO review (user_id, product_id, rating, comment, created_at, updated_at)
VALUES
    (1, 1, 5, '맛있어요!', NOW(), NOW()),
    (2, 1, 4, '괜찮아요', NOW(), NOW()),
    (3, 2, 5, '좋아요', NOW(), NOW()),
    (4, 3, 3, '별로에요', NOW(), NOW()),
    (1, 4, 4, '운동하기 좋아요', NOW(), NOW());

INSERT INTO users (email, password, nickname, point, image_url, created_at, updated_at, is_admin)
VALUES
    ('user1@example.com', 'password1', 'user1', 1000, 'user1.jpg', NOW(), NOW(), false),
    ('user2@example.com', 'password2', 'user2', 1500, 'user2.jpg', NOW(), NOW(), false),
    ('user3@example.com', 'password3', 'user3', 5000, 'user3.jpg', NOW(), NOW(), false),
    ('user4@example.com', 'password4', 'user4', 1000, 'user4.jpg', NOW(), NOW(), false);