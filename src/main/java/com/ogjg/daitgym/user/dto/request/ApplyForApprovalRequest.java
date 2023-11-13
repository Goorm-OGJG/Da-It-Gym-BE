package com.ogjg.daitgym.user.dto.request;

import com.ogjg.daitgym.domain.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ApplyForApprovalRequest {

    private List<CertificationDto> certifications;
    private List<AwardsDto> awards;

    public ApplyForApprovalRequest(List<CertificationDto> certifications, List<AwardsDto> awards) {
        this.certifications = certifications;
        this.awards = awards;
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class CertificationDto {
        private String name;
        private LocalDate acquisitionAt;

        public CertificationDto(String name, LocalDate acquisitionAt) {
            this.name = name;
            this.acquisitionAt = acquisitionAt;
        }

        public Certification toCertification(User user, Approval approval) {
            return Certification.builder()
                    .user(user)
                    .approval(approval)
                    .name(this.name)
                    .acquisitionAt(this.acquisitionAt)
                    .certificationImages(new ArrayList<>())
                    .build();
        }

        private List<CertificationImage> toCertificationImgs(List<String> imgUrls) {
            return imgUrls.stream()
                    .map((imgUrl) ->
                            CertificationImage.builder()
                                    .url(imgUrl)
                                    .build())
                    .toList();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class AwardsDto {
        private String name;
        private LocalDate awardAt;
        private String org;

        public Award toAward(User user, Approval approval) {
            return Award.builder()
                    .user(user)
                    .approval(approval)
                    .awardName(this.name)
                    .awardAt(this.awardAt)
                    .hostOrganization(this.org)
                    .awardImages(new ArrayList<>())
                    .build();
        }

        private static List<AwardImage> toAwardImages(List<String> imgUrls) {
            return imgUrls.stream()
                    .map((imgUrl) ->
                            AwardImage.builder()
                                    .url(imgUrl)
                                    .build()
                    ).toList();
        }

    }
}
