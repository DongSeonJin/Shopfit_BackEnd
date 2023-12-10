package com.spring.community.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class ImgUploadToRDS {// imgUrl을 이미지 파일로 변환해주는 코드
    private static final String BUCKET_NAME = "shopfit-community-img";
    private static final String AWS_ACCESS_KEY = "AKIA56N4DSBPUIG2AQFZ";
    private static final String AWS_SECRET_KEY = "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";
    private static final String AWS_REGION = "ap-northeast-2";


    private static final String RDS_URL = "jdbc:mysql://database-1.c05ixap9tiuz.ap-northeast-2.rds.amazonaws.com:3306/nc06-final-project?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String USERNAME = "TeamWorkout";
    private static final String PASSWORD = "shopfitdb123";

    public static List<String> getImageUrls(){
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(AWS_REGION)
                .build();


        ListObjectsV2Result result = s3Client.listObjectsV2(BUCKET_NAME, "instagram_img/");
        List<String> imageUrls = new ArrayList<>();
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            String imageUrl = "https://" + BUCKET_NAME + ".s3.ap-northeast-2.amazonaws.com/" + objectSummary.getKey();
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }


    public static void storeImageUrls(List<String> imageUrls) {
        int count = 500;
        try (Connection conn = DriverManager.getConnection(RDS_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE post SET image_url2 = ? WHERE post_id = ? AND image_url2 IS NOT NULL";
            for (String url : imageUrls) {
                count--;
                System.out.println(count + "번째 이미지 삽입");
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setString(1, url);
                    preparedStatement.setInt(2, count);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args){
        List<String> imageUrls = getImageUrls();
        storeImageUrls(imageUrls);

    }
}
