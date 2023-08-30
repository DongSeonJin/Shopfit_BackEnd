package com.spring.shopping.controller;

import com.spring.shopping.component.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 파일 1개 업로드
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        // 업로드된 파일을 AmazonS3에 업로드하고
        String fileUrl = s3Uploader.uploadFiles(multipartFile, "static");

        // URL을 가져옴
        return fileUrl;
    }


    // 파일 1개 삭제
    @DeleteMapping("/delete/{objectKey}")
    public String deleteObject(@PathVariable String objectKey) {

        // 경로 직접 설정
        String dir = "static/";

        // Amazon S3에서 객체 삭제
        s3Uploader.deleteFile(dir + objectKey);

        return "Object deleted successfully";
    }






}
