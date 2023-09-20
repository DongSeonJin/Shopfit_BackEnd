package com.spring.S3;

import com.spring.S3.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class S3Controller {

    private final S3Uploader s3Uploader;

    @Autowired
    public S3Controller(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @Value("${cdn_base_path}")
    private String cdnBasePath;

    @Value("${nc_base_path}")
    private String ncBasePath;

    // 파일 1개 업로드
    @PostMapping
    public String uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        // 업로드된 파일을 AmazonS3에 업로드하고
        String fileUrl = s3Uploader.uploadFiles(multipartFile, "static");

        // URL을 가져옴
        return fileUrl;
    }

    // 1. 상품 썸네일 업로드 (이미지 최적화 적용 -> 리사이즈 & 크롭 480*480)
    // 2. 상품 상세이미지 업로드 (이미지 최적화 적용 -> 가로기준(720) 변경, 강제확대 설정)
    // 3. 유저 프로필이미지 업로드 ( 이미지 최적화 적용 -> 리사이즈 & 크롭 200*200)
    @PostMapping("/imageOptimizer/{option}")
    public String uploadOptimizedImage(@RequestParam("file") MultipartFile multipartFile, @PathVariable int option) throws IOException {

        // 업로드된 파일을 AmazonS3에 업로드하고 url 가져오기
        String originalUrl = s3Uploader.uploadFiles(multipartFile, "static");

        // URL에서 경로를 추출하기
        String basePath = cdnBasePath;
        String path = originalUrl.replaceFirst(ncBasePath, "");

        // 이미지 최적화 옵션 적용하기
        String newUrl = basePath + path + getImageOptimizerOption(option);

        // 이미지 최적화 마친 코드 반환하기
        return newUrl;
    }

    // 이미지 최적화 옵션 선택을 위한 메서드
    private String getImageOptimizerOption(int option) {
        if (option == 1) { // 1. 상품 썸네일 업로드 (이미지 최적화 적용 -> 리사이즈 & 크롭 480*480)
            return "?type=f&w=480&h=480&align=4&faceopt=true&quality=100&anilimit=100";
        } else if (option == 2 ){  //2. 상품 상세이미지 업로드 (이미지 최적화 적용 -> 가로기준(720) 변경, 강제확대 설정)
            return "?type=w&w=720&quality=100&extopt=3&anilimit=100";
        } else if (option == 3) { //3. 프로필 이미지 업로드 (이미지 최적화 적용 -> 리사이즈 & 크롭 350*350)
            return "?type=f&w=350&h=350&quality=100&align=4&anilimit=100";
        } else {
            throw new IllegalArgumentException("잘못된 이미지 최적화 옵션입니다.");
        }
    }


    // 파일 1개 삭제
    @DeleteMapping("/{objectKey}")
    public String deleteObject(@PathVariable String objectKey) {

        // 경로 직접 설정
        String dir = "static/";

        // Amazon S3에서 객체 삭제
        s3Uploader.deleteFile(dir + objectKey);

        return "Object deleted successfully";
    }






}
