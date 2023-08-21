CREATE TABLE post (
          post_id long,
          user_id integer,
          nickname varchar(30),
          category_id int,
          title varchar(30),
          content text,
          view_count integer,
          created_at timestamp,
          updated_at timestamp,
          image_url1 text,
          image_url2 text,
          image_url3 text
);


INSERT INTO post (post_id, user_id, nickname, category_id, title, content, view_count, created_at, updated_at, image_url1, image_url2, image_url3)
VALUES
    (1, 101, 'user1', '1', '첫 번째 포스트', '첫 번째 포스트 내용입니다.', 150, '2023-08-20 10:00:00', '2023-08-20 10:30:00', 'image1.jpg', 'image2.jpg', 'image3.jpg'),
    (2, 102, 'user2', '2', '두 번째 포스트', '두 번째 포스트 내용입니다.', 200, '2023-08-20 11:00:00', '2023-08-20 11:45:00', 'image4.jpg', 'image5.jpg', 'image6.jpg'),
    (3, 103, 'user3', '3', '세 번째 포스트', '세 번째 포스트 내용입니다.', 80, '2023-08-20 12:00:00', '2023-08-20 12:20:00', 'image7.jpg', 'image8.jpg', 'image9.jpg');



