package com.spring.community.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Droptable {
    public static void main(String[] args) {
        String URL = "";
        String USER = "";
        String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement stmt = conn.createStatement();

            // post 테이블에서 모든 postId를 조회
            ResultSet rs = stmt.executeQuery("SELECT post_id FROM post");

            List<Integer> postIds = new ArrayList<>();

            while (rs.next()) {
                postIds.add(rs.getInt(1));
            }

            // 각 postId에 대해 동적으로 like_{postId} 형식의 테이블을 생성
            for (Integer postId : postIds) {
                stmt.execute("CREATE TABLE IF NOT EXISTS likes_" + postId + " (" +
                        "like_id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                        "user_id BIGINT NOT NULL," +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                        ")");
                System.out.println("Created table: likes_" + postId);
            }

        } catch (SQLException e) {
            System.out.println("Error in database operation: " + e);
        }
    }
}
