package com.ogjg.daitgym.admin.dto.response;

import com.ogjg.daitgym.domain.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
public class GetApprovalDetailResponse {

    private GetApprovalDetail approval;
    private List<GetApprovalDetailAward> awards;
    private List<GetApprovalDetailCertification> certifications;
    private List<String> awardImgs;
    private List<String> certificationImgs;

    @Builder
    public GetApprovalDetailResponse(GetApprovalDetail approval, List<GetApprovalDetailAward> awards, List<GetApprovalDetailCertification> certifications, List<String> awardImgs, List<String> certificationImgs) {
        this.approval = approval;
        this.awards = awards;
        this.certifications = certifications;
        this.awardImgs = awardImgs;
        this.certificationImgs = certificationImgs;
    }

    public static GetApprovalDetailResponse from(Approval approval) {
        return GetApprovalDetailResponse.builder()
                .approval(GetApprovalDetail.from(approval))
                .awards(toDetailAwards(approval))
                .certifications(toDetailCertifcations(approval))
                .awardImgs(toDetailAwardImageUrls(approval))
                .certificationImgs(toDetailCertificationImageUrls(approval))
                .build();
    }

    private static User findUser(Approval approval) {
        if (approval.getAwards() == null) {
            return approval.getCertifications().get(0).getUser();
        }

        return approval.getAwards().get(0).getUser();
    }

    private static List<GetApprovalDetailCertification> toDetailCertifcations(Approval approval) {
        return approval.getCertifications().stream()
                .map((GetApprovalDetailCertification::from))
                .toList();
    }

    private static List<GetApprovalDetailAward> toDetailAwards(Approval approval) {
        return approval.getAwards().stream()
                .map((GetApprovalDetailAward::from))
                .toList();
    }

    private static List<String> toDetailAwardImageUrls(Approval approval) {
        return approval.getAwards().stream()
                .map(Award::getAwardImages)
                .flatMap(Collection::stream)
                .map(AwardImage::getUrl)
                .toList();
    }

    private static List<String> toDetailCertificationImageUrls(Approval approval) {
        return approval.getCertifications().stream()
                .map(Certification::getCertificationImages)
                .flatMap(Collection::stream)
                .map(CertificationImage::getUrl)
                .toList();
    }

    @Getter
    public static class GetApprovalDetail {
        private Long approvalId;
        private String nickname;
        private String email;
        private String approvalStatus;
        private boolean withdraw;
        private LocalDate joinAt;
        private String reason;

        @Builder
        public GetApprovalDetail(Long approvalId, String nickname, String email, String approvalStatus, boolean withdraw, LocalDate joinAt, String reason) {
            this.approvalId = approvalId;
            this.nickname = nickname;
            this.email = email;
            this.approvalStatus = approvalStatus;
            this.withdraw = withdraw;
            this.joinAt = joinAt;
            this.reason = reason;
        }

        public static GetApprovalDetail from(Approval approval) {
            User user = findUser(approval);

            return GetApprovalDetail.builder()
                   .approvalId(approval.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .approvalStatus(approval.getApproveStatus().getTitle())
                .withdraw(user.isDeleted())
                .joinAt(user.getCreatedAt().toLocalDate())
                .reason(approval.getContent())
                .build();
        }
    }

    @Getter
    public static class GetApprovalDetailAward {
        private String name;
        private LocalDate awardAt;
        private String org;

        @Builder
        public GetApprovalDetailAward(String name, LocalDate awardAt, String org) {
            this.name = name;
            this.awardAt = awardAt;
            this.org = org;
        }

        public static GetApprovalDetailAward from(Award award) {
            return GetApprovalDetailAward.builder()
                    .name(award.getAwardName())
                    .awardAt(award.getAwardAt())
                    .org(award.getHostOrganization())
                    .build();
        }
    }

    @Getter
    public static class GetApprovalDetailCertification {
        private String name;
        private LocalDate acquisitionAt; // acquire 변경 요망

        @Builder
        public GetApprovalDetailCertification(String name, LocalDate acquisitionAt) {
            this.name = name;
            this.acquisitionAt = acquisitionAt;
        }

        public static GetApprovalDetailCertification from(Certification certification) {
            return GetApprovalDetailCertification.builder()
                    .name(certification.getName())
                    .acquisitionAt(certification.getAcquisitionAt())
                    .build();
        }
    }
}
