package com.spring.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    // 파일 업로드 클래스 생성
    // 업로드할 때 파일이 로컬에 없으면 에러(Unable to calculate MD5 hash: [파일명] (No such file or directory) 발생하기 때문에
    // convert로 입력받은 파일을 로컬에 저장하고
    // upload로 S3 버킷에 업로드하기

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
        // multupartFile을 File로 변환하기
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        // 업로드된 File과 디렉토리 이름을 사용하여 업로드를 수행하고 S3 URL을 반환하기
        return upload(uploadFile, dirName);
    }

    public String upload(File uploadFile, String filePath) {
        //S3에 저장될 파일 이름을 생성하기. 파일 경로, UUID, 파일 이름을 조합하기
        String fileName = filePath + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름

        // S3로 파일을 업로드하고 업로드된 파일의 S3 URL을 반환하기
        String uploadImageUrl = putS3(uploadFile, fileName);

        // 업로드가 완료되면 로컬 파일을 삭제하기
        removeNewFile(uploadFile);

        // 업로드된 파일의 S3URL을 반환하기
        return uploadImageUrl;
    }

    // S3로 파일 업로드
    private String putS3(File uploadFile, String fileName) {
        // Amazon S3 클라이언트를 사용하여 파일을 S3 버킷에 업로드하기
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));

        // 업로드된 파일의 S3URL을 반환하기
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            // 파일 삭제 성공 시 메시지 출력
            System.out.println("File delete success");
            return;
        }
        // 파일 삭제 실패 시 메시지 출력
        System.out.println("File delete fail");
    }

    // MultipartFile을 로컬 파일로 변환하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        // 변환된 파일의 경로 생성하기(현재 작업 디렉토리의 경로)
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        // 변환된 파일을 생성하기
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                // MultipartFile의 내용을 변환된 파일에 기록
                fos.write(file.getBytes());
            }
            // 변환된 파일을 Optional 객체로 래핑하여 반환하기
            return Optional.of(convertFile);
        }
        // 파일 생성 실패 시 빈 Optional을 반환
        return Optional.empty();
    }

    public void deleteFile(String objectKey) {
        // Amazon S3에서 객체를 삭제합니다.
        // objectKey = 파일 이름
        amazonS3Client.deleteObject(bucket, objectKey);
        System.out.println("File deleted from S3: " + objectKey);
    }


}
