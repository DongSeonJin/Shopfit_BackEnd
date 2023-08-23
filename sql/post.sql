insert into post_category (category_name)
values
    ('오운완'),
    ('식단'),
    ('자유게시판');

    INSERT INTO users (email, password, nickname, point, image_url, created_at, updated_at, is_admin) VALUES
    ('user01@example.com', 'password01', 'nickname01', 0, 'https://example.com/image01.jpg', NOW(), NOW(), 0),
    ('user02@example.com', 'password02', 'nickname02', 0, 'https://example.com/image02.jpg', NOW(), NOW(), 0),
    ('user03@example.com', 'password03', 'nickname03', 0, 'https://example.com/image03.jpg', NOW(), NOW(), 0),
    ('user04@example.com', 'password04', 'nickname04', 0, 'https://example.com/image04.jpg', NOW(), NOW(), 0),
    ('user05@example.com', 'password05', 'nickname05', 0, 'https://example.com/image05.jpg', NOW(), NOW(), 0),
    ('user06@example.com', 'password06', 'nickname06', 0, 'https://example.com/image06.jpg', NOW(), NOW(), 0),
    ('user07@example.com', 'password07', 'nickname07', 0, 'https://example.com/image07.jpg', NOW(), NOW(), 0),
    ('user08@example.com', 'password08', 'nickname08', 0, 'https://example.com/image08.jpg', NOW(), NOW(), 0),
    ('user09@example.com', 'password09', 'nickname09', 0, 'https://example.com/image09.jpg', NOW(), NOW(), 0),
    ('user10@example.com', 'password10', 'nickname10', 0, 'https://example.com/image10.jpg', NOW(), NOW(), 0),
    ('user11@example.com', 'password11', 'nickname11', 0, 'https://example.com/image11.jpg', NOW(), NOW(), 0),
    ('user12@example.com', 'password12', 'nickname12', 0, 'https://example.com/image12.jpg', NOW(), NOW(), 0),
    ('user13@example.com', 'password13', 'nickname13', 0, 'https://example.com/image13.jpg', NOW(), NOW(), 0),
    ('user14@example.com', 'password14', 'nickname14', 0, 'https://example.com/image14.jpg', NOW(), NOW(), 0),
    ('user15@example.com', 'password15', 'nickname15', 0, 'https://example.com/image15.jpg', NOW(), NOW(), 0),
    ('user16@example.com', 'password16', 'nickname16', 0, 'https://example.com/image16.jpg', NOW(), NOW(), 0),
    ('user17@example.com', 'password17', 'nickname17', 0, 'https://example.com/image17.jpg', NOW(), NOW(), 0),
    ('user18@example.com', 'password18', 'nickname18', 0, 'https://example.com/image18.jpg', NOW(), NOW(), 0),
    ('user19@example.com', 'password19', 'nickname19', 0, 'https://example.com/image19.jpg', NOW(), NOW(), 0),
    ('user20@example.com', 'password20', 'nickname20', 0, 'https://example.com/image20.jpg', NOW(), NOW(), 0),
    ('user21@example.com', 'password21', 'nickname21', 0, 'https://example.com/image21.jpg', NOW(), NOW(), 0),
    ('user22@example.com', 'password22', 'nickname22', 0, 'https://example.com/image22.jpg', NOW(), NOW(), 0),
    ('user23@example.com', 'password23', 'nickname23', 0, 'https://example.com/image23.jpg', NOW(), NOW(), 0),
    ('user24@example.com', 'password24', 'nickname24', 0, 'https://example.com/image24.jpg', NOW(), NOW(), 0),
    ('user25@example.com', 'password25', 'nickname25', 0, 'https://example.com/image25.jpg', NOW(), NOW(), 0),
    ('user26@example.com', 'password26', 'nickname26', 0, 'https://example.com/image26.jpg', NOW(), NOW(), 0),
    ('user27@example.com', 'password27', 'nickname27', 0, 'https://example.com/image27.jpg', NOW(), NOW(), 0),
    ('user28@example.com', 'password28', 'nickname28', 0, 'https://example.com/image28.jpg', NOW(), NOW(), 0),
    ('user29@example.com', 'password29', 'nickname29', 0, 'https://example.com/image29.jpg', NOW(), NOW(), 0),
    ('user30@example.com', 'password30', 'nickname30', 0, 'https://example.com/image30.jpg', NOW(), NOW(), 0),
    ('user96@example.com', 'password96', 'nickname96', 0, 'https://example.com/image96.jpg', NOW(), NOW(), 0),
    ('user97@example.com', 'password97', 'nickname97', 0, 'https://example.com/image97.jpg', NOW(), NOW(), 0),
    ('user98@example.com', 'password98', 'nickname98', 0, 'https://example.com/image98.jpg', NOW(), NOW(), 0),
    ('user99@example.com', 'password99', 'nickname99', 0, 'https://example.com/image99.jpg', NOW(), NOW(), 0),
    ('user100@example.com', 'password100', 'nickname100', 0, 'https://example.com/image100.jpg', NOW(), NOW(), 0);


INSERT INTO post (user_id, category_id, nickname, title, content, view_count, created_at, updated_at, image_url1, image_url2, image_url3)
VALUES
    (1, 1, '닉네임1', '제목 1', '내용 1', 0, NOW(), NOW(), '이미지 URL 1', NULL, NULL),
    (2, 2, '닉네임2', '제목 2', '내용 2', 0, NOW(), NOW(), '이미지 URL 2', NULL, NULL),
    (2, 2, '닉네임3', '제목 3', '내용 3', 0, NOW(), NOW(), '이미지 URL 3', NULL, NULL),
    (3, 1, '닉네임4', '제목 4', '내용 4', 0, NOW(), NOW(), '이미지 URL 4', NULL, NULL),
    (1, 1, '닉네임5', '제목 5', '내용 5', 0, NOW(), NOW(), '이미지 URL 5', NULL, NULL),
    (2, 2, '닉네임6', '제목 6', '내용 6', 0, NOW(), NOW(), '이미지 URL 6', NULL, NULL),
    (2, 2, '닉네임7', '제목 7', '내용 7', 0, NOW(), NOW(), '이미지 URL 7', NULL, NULL),
    (3, 1, '닉네임8', '제목 8', '내용 8', 0, NOW(), NOW(), '이미지 URL 8', NULL, NULL),
    (1, 1, '닉네임9', '제목 9', '내용 9', 0, NOW(), NOW(), '이미지 URL 9', NULL, NULL),
    (2, 2, '닉네임10', '제목 10', '내용 10', 0, NOW(), NOW(), '이미지 URL 10', NULL, NULL);