package com.spring.community.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConverUrlCreateLike {
    public static void main(String[] args) {


        String URL = "";
        String USER = "";
        String PASSWORD = "";



        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT table_name \n" +
                        "FROM information_schema.tables \n" +
                        "WHERE table_schema = 'nc06-final-project' AND table_name LIKE 'post_%';");  // Replace with your SQL query

                List<String> tableNames = new ArrayList<>();
                List<String> nicknames = new ArrayList<>();

                // ResultSet으로부터 모든 테이블 이름 가져오기
                while (rs.next()) {
                    tableNames.add(rs.getString(1));
                    nicknames.add(rs.getString(1).replace("post_", ""));
                }

                tableNames.remove("post_category");
                nicknames.remove("category");

                System.out.println("테이블이름: " + tableNames);
                System.out.println("닉네임 :" + nicknames);
                List<String> url1 = new ArrayList<>();
                List<String> url2 = new ArrayList<>();

                // 추출한 닉네임별로 url저장.
                for(int i = 0; i < nicknames.size(); i++){
//                    Statement stmt1 = conn.createStatement();
//                    ResultSet rt = stmt1.executeQuery("SELECT image_url1 FROM post WHERE nickname ='" + nicknames.get(i) +"'");  // Replace with your SQL query
//
//
//                    String url = null;
//                    if (rt.next()) {
//                        url = rt.getString(1);
//                        //System.out.println(url);
//                    }
//
//                    Statement stmt2 = conn.createStatement();
//                    ResultSet rt2 = stmt2.executeQuery("SELECT image_url2 FROM post WHERE nickname ='" + nicknames.get(i) +"'");  // Replace with your SQL query
//                    String imgurl2 = null;
//                    if(rt2.next()){
//                        imgurl2 = rt2.getString(1);
//                    }
//
//
//                    System.out.println("imageUrl1 변환성공, imageUrl1 :" + url);
//                    System.out.println("imageUrl2 변환성공, imageUrl2 :" + imgurl2);
//                    System.out.println("테이블 이름 :" + tableNames.get(i));
//                    System.out.println(i + "번째 변환성공======================================================");
//
//
//                    PreparedStatement stmt3 = conn.prepareStatement("UPDATE " + tableNames.get(i) + " SET image_url1=?, image_url2=?");
//                    stmt3.setString(1,url);
//                    stmt3.setString(2,imgurl2);
//
//                    int updatedRows = stmt3.executeUpdate();

//                    while (rt.next()){
//                        url1.add(rt.getString(1));
//                    }

                    PreparedStatement stmt4 = conn.prepareStatement("CREATE TABLE likes_" + nicknames.get(i) + "(like_id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT, post_id BIGINT, dynamic_post_id BIGINT," +
                            " createdAt DATETIME, FOREIGN KEY (user_id) REFERENCES users(user_id) " +
                            "ON DELETE CASCADE, FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE)");

                    int createTables = stmt4.executeUpdate();
                }

                //System.out.println("imageurl1 :" + url1);

//                for (String tableName : tableNames) {
//                    PreparedStatement stmt2 = conn.prepareStatement("UPDATE " + tableName + " SET image_url1=? WHERE image_url1=?");
//                    stmt2.setString(1,s3Url);
//                    stmt2.setString(2,imageUrl);
//                    int updatedRows = stmt2.executeUpdate();
//                }


            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
