package com.ogjg.daitgym.s3.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class S3Repository {

    @Value("${cloud.aws.credentials.bucket-name}")
    private String bucketName;
    private final S3Client s3Client;


    public String uploadImageToS3(MultipartFile file) {
        String uploadUrl = null;
        try {
            String uploadFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uploadFilename)
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(
                            file.getInputStream(), file.getSize()
                    )
            );

            uploadUrl = getObjectUrl(uploadFilename);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return uploadUrl;
    }

    private String getObjectUrl(String fileName) {
        return s3Client.utilities()
                .getUrl(GetUrlRequest
                        .builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build())
                .toExternalForm();
    }

    public void deleteImageFromS3(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String key = url.getPath().substring(1);
            String filename = URLDecoder.decode(key, StandardCharsets.UTF_8);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
