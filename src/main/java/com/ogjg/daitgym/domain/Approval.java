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
    private String managerEmail;


    @Builder
    public Approval(Long id, List<Certification> certifications, List<Award> awards, ApproveStatus approveStatus, String content, String managerEmail) {
        this.id = id;
        this.certifications = certifications;
        this.awards = awards;
        this.approveStatus = approveStatus;
        this.content = content;
        this.managerEmail = managerEmail;
    }

    public void edit(ApproveStatus approveStatus, String reason, String loginEmail) {
        this.approveStatus = approveStatus;
        this.content = reason == null ? "" : reason;
        this.managerEmail = loginEmail;
        // todo : mangerId 등록
    }

    /**
     *
     * @param awards
     * awards를 get할때, null이 될 가능성이 있다.
     * awards가 비어있다면 애초에 awardImageUrls도 빈 값이 넘어오도록 설계되어 있다. addCertifications 메서드도 마찬가지이다.
     */
    public void addAwards(List<Award> awards, List<String> awardImageUrls) {
        if (awards.isEmpty()) return;

        this.awards = awards;
        awards.stream().forEach((award -> award.addApproval(this)));

        awards.get(FIRST_ELEMENT).addImageUrls(awardImageUrls);
    }

    public void addCertifications(List<Certification> certifications, List<String> certificationImageUrls) {
        if (certifications.isEmpty()) return;

        this.certifications = certifications;
        certifications.stream().forEach((certification -> certification.addApproval(this)));
        certifications.get(FIRST_ELEMENT).addCertificationImageUrls(certificationImageUrls);
    }

    public boolean isProceeding() {
        return approveStatus == ApproveStatus.WAITING || approveStatus == ApproveStatus.SUSPENSION;
    }
}
