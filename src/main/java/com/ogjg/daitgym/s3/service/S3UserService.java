package com.ogjg.daitgym.s3.service;

import com.ogjg.daitgym.s3.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3UserService {

    @Value("${cloud.aws.default.profile-img}")
    private String AWS_DEFAULT_PROFILE_IMG;

    private final S3Repository s3Repository;

    public String saveProfileImage(MultipartFile multipartFile, String currentProfileImageUrl) {

        // 기존 이미지를 사용하던 유저가 새 이미지를 저장시 default 이미지를 삭제하는 것을 막는다.
        if (!AWS_DEFAULT_PROFILE_IMG.equals(currentProfileImageUrl)) {
            s3Repository.deleteImageFromS3(currentProfileImageUrl);
        }

        return s3Repository.uploadImageToS3(multipartFile);
    }

    /**
     * 자격증과 이미지 혹은 수상과 이미지가 모두 값이 존재           -->  s3에 저장하고 url들을 반환
     * 자격증과 이미지 혹은 수상과 이미지가 모두 null 혹은 비어있다면 -->  빈 리스트 반환
     */
    public List<String> saveCareerImages(Collection<?> submitted, List<MultipartFile> imageFiles) {
        if (!isEmptyCollection(submitted) && !isFileListNull(imageFiles)) {
            return saveImages(imageFiles);
        }

        return Collections.emptyList();
    }

    private List<String> saveImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream()
                .map(s3Repository::uploadImageToS3)
                .toList();
    }

    private boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    // List<MultipartFile>은 빈 파일보내면 길이 1의 리스트를 반환
    private boolean isFileListNull(List<MultipartFile> imgFiles) {
        return imgFiles == null || imgFiles.get(0).isEmpty();
    }

}
