package com.ogjg.daitgym.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Approval extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "approval")
    private List<Certification> certifications = new ArrayList<>();

    @OneToMany(mappedBy = "approval")
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
}
