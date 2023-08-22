
-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
-- user, post, reply 순서로 insert 해야함.

-- User 더미 데이터 삽입
INSERT INTO users (email, password, nickname, point, image_url, created_at, updated_at, is_admin)
VALUES ('user1@example.com', 'password1', 'User1', 100, 'user1.jpg', '2023-08-21 12:34:56', '2023-08-21 12:34:56', false),
       ('user2@example.com', 'password2', 'User2', 200, 'user2.jpg', '2023-08-21 12:34:56', '2023-08-21 12:34:56', false),
       ('user3@example.com', 'password3', 'User3', 300, 'user3.jpg', '2023-08-21 12:34:56', '2023-08-21 12:34:56', false);

SELECT * FROM users;

-- Post 더미 데이터 삽입
INSERT INTO post (post_id, user_id, nickname, title, content, view_count, created_at, updated_at, image_url1, image_url2, image_url3)
VALUES (101, 1, 'User1', 'First Post', 'This is the content of the first post.', 0, '2023-08-21 12:34:56', '2023-08-21 12:34:56', 'image1.jpg', null, null),
       (102, 2, 'User2', 'Second Post', 'This is the content of the second post.', 0, '2023-08-21 12:34:56', '2023-08-21 12:34:56', 'image2.jpg', null, null),
       (103, 3, 'User3', 'Third Post', 'This is the content of the third post.', 0, '2023-08-21 12:34:56', '2023-08-21 12:34:56', 'image3.jpg', null, null);

SELECT * FROM post;

-- Reply 더미 데이터 삽입
INSERT INTO reply (post_id, user_id, content, created_at, updated_at)
VALUES (101, 1, 'This is a reply to the first post.', '2023-08-21 12:34:56', '2023-08-21 12:34:56'),
       (102, 2, 'This is a reply to the second post.', '2023-08-21 12:34:56', '2023-08-21 12:34:56'),
       (102, 3, '3이 글번호 102에 댓글', '2023-08-21 12:34:56', '2023-08-21 12:34:56'),
       (103, 3, 'This is a reply to the third post.', '2023-08-21 12:34:56', '2023-08-21 12:34:56');

SELECT * FROM reply;