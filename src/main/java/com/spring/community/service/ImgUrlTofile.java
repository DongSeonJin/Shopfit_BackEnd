package com.spring.community.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class ImgUrlTofile {// imgUrl을 이미지 파일로 변환해주는 코드


    public static void main(String[] args){
        String accessKey = "";
        String secretKey = "";
        String region = "";
        String bucketName = "";
        String filePath = "";
        String endPoint = "";


        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        // Create an Amazon S3 client

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .build();



        String URL = "";
        String USER = "";
        String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT image_url2 FROM post where nickname != 'nickname1' AND image_url2 is not null");  // Replace with your SQL query

            List<String> imageUrls = new ArrayList<>();

            while (rs.next()) {
                System.out.println(rs.getString("image_url2"));
                imageUrls.add(rs.getString("image_url2"));
            }
            int filenumb = 0;

            for (String imageUrl : imageUrls) {

                    URL url = new URL(imageUrl);
                    String fileName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);

                try (InputStream in = url.openStream()){
                    filenumb++;
                    Path outputPath = Paths.get(filePath, fileName); // Replace with your directory
                    Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);

                    File fileToUpload = outputPath.toFile();

                    // 여기서 S3 업로드를 수행합니다.
                    s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileToUpload));

                    // After the upload is successful, get the URL of the uploaded object.
                    String s3Url = s3Client.getUrl(bucketName, fileName).toString();

                    System.out.println(filenumb + "번째 이미지파일 url로 변환 성공: " + s3Url);


                    PreparedStatement stmt2 = conn.prepareStatement("UPDATE post SET image_url2=? WHERE image_url2=?");
                    stmt2.setString(1,s3Url);
                    stmt2.setString(2,imageUrl);
                    int updatedRows = stmt2.executeUpdate();


                    // Delete the local file after upload.
                    if(fileToUpload.delete()) {
                        System.out.println("Local file deleted successfully.");
                    } else {
                        System.out.println("Failed to delete local file.");
                    }

//                    String localImageUrl = outputPath.toUri().toString(); // This is the local URL. Save this to DB.
//                    System.out.println("Local Image URL: " + localImageUrl);
//                    System.out.println(filenumb + "번째 사진 다운 완료");


                }catch (NullPointerException e){
                    System.out.println("null값인 게시글 발견");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
