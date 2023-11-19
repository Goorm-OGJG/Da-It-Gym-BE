package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Approval extends BaseEntity {

    private static final int FIRST_ELEMENT = 0;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "approval", cascade = ALL, orphanRemoval = true)
    private List<Certification> certifications = new ArrayList<>();

    @OneToMany(mappedBy = "approval", cascade = ALL, orphanRemoval = true)
    private List<Award> awards = new ArrayList<>();

    private ApproveStatus approveStatus;

    private String content;

    // Todo 승인 관리자 연관관계에 대해 고민해보기
    private Long approvalManager;

    @Builder
    public Approval(Long id, List<Certification> certifications, List<Award> awards, ApproveStatus approveStatus, String content, Long approvalManager) {
        this.id = id;
        this.certifications = certifications;
        this.awards = awards;
        this.approveStatus = approveStatus;
        this.content = content;
        this.approvalManager = approvalManager;
    }

    public void edit(String approveStatus, String reason, String loginEmail) {
        this.approveStatus = ApproveStatus.from(approveStatus);
        this.content = reason;
        // todo : mangerId 등록
    }

    public void addAwards(List<Award> awards, List<String> awardImageUrls) {
        this.awards = awards;
        awards.stream().forEach((award -> award.addApproval(this)));

        awards.get(FIRST_ELEMENT).addImageUrls(awardImageUrls);
    }

    public void addCertifications(List<Certification> certifications, List<String> certificationImageUrls) {
        this.certifications = certifications;
        certifications.stream().forEach((certification -> certification.addApproval(this)));

        certifications.get(FIRST_ELEMENT).addCertificationImageUrls(certificationImageUrls);
    }
}
