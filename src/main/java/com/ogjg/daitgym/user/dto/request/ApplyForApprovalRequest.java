package com.ogjg.daitgym.user.dto.request;

import com.ogjg.daitgym.domain.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<Award> toAwards(User user) {
        if (awards == null) return Collections.emptyList();

        return awards.stream()
                .map((dto) -> dto.toAward(user))
                .toList();
    }

    public List<Certification> toCertifications(User user) {
        if (certifications == null) return Collections.emptyList();

        return certifications.stream()
                .map((dto) -> dto.toCertification(user))
                .toList();
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

        public Certification toCertification(User user) {
            return Certification.builder()
                    .user(user)
                    .name(this.name)
                    .acquisitionAt(this.acquisitionAt)
                    .certificationImages(new ArrayList<>())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class AwardsDto {
        private String name;
        private LocalDate awardAt;
        private String org;

        public Award toAward(User user) {
            return Award.builder()
                    .user(user)
                    .awardName(this.name)
                    .awardAt(this.awardAt)
                    .hostOrganization(this.org)
                    .awardImages(new ArrayList<>())
                    .build();
        }
    }
}
