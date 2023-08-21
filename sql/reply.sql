CREATE TABLE reply (
                       reply_id INT AUTO_INCREMENT PRIMARY KEY,
                       post_id INT NOT NULL,
                       user_id INT NOT NULL,
                       content TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE,
                       FOREIGN KEY (user_id) REFERENCES user (user_id)
);

INSERT INTO reply (post_id, user_id, content, created_at, updated_at)
VALUES
    (1, 1, '첫 번째 댓글', now(), now()),
    (1, 2, '두 번째 댓글', now(), now()),
    (2, 1, '세 번째 댓글', now(), now());

SELECT * FROM reply;

INSERT INTO users (email, password, nickname, point, image_url, created_at, updated_at, is_admin, is_deleted)
VALUES
    ('user1@email.com', 'password1', 'nickname1', 100, 'image_url1', NOW(), NOW(), FALSE, FALSE),
    ('user2@email.com', 'password2', 'nickname2', 200, 'image_url2', NOW(), NOW(), FALSE, FALSE);

SELECT * FROM users;