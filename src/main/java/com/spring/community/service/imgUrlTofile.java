package com.spring.community.service;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class imgUrlTofile {

    public static void main(String[] args){

        String URL = "jdbc:mysql://db-ibfto-kr.vpc-pub-cdb.ntruss.com:3306/nc06-final-project?serverTimezone=UTC&characterEncoding=UTF-8";
        String USER = "master";
        String PASSWORD = "naverclouddb123!@#";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT image_url1 FROM post where nickname != 'nickname1'");  // Replace with your SQL query

            List<String> imageUrls = new ArrayList<>();

            while (rs.next()) {
                System.out.println(rs.getString("image_url1"));
                imageUrls.add(rs.getString("image_url1"));
            }
            int filenumb = 0;

            for (String imageUrl : imageUrls) {

                    URL url = new URL(imageUrl);
                    String fileName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);

                try (InputStream in = url.openStream()){
                    Path outputPath = Paths.get("C:\\workspace\\instagram_img", fileName); // Replace with your directory
                    Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);

                    String localImageUrl = outputPath.toUri().toString(); // This is the local URL. Save this to DB.
                    System.out.println("Local Image URL: " + localImageUrl);
                    filenumb++;
                    System.out.println(filenumb + "번째 사진 다운 완료");

                }catch (NullPointerException e){
                    System.out.println("null값인 게시글 발견");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }





    }
}
