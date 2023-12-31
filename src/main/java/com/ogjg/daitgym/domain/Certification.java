package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Certification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @OneToMany(mappedBy = "certification", cascade = ALL, orphanRemoval = true)
    private List<CertificationImage> certificationImages = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "approval_id")
    private Approval approval;

    private String name;

    private String issuingOrganization;

    private LocalDate acquisitionAt;

    @Builder
    public Certification(Long id, User user, List<CertificationImage> certificationImages, Approval approval, String name, String issuingOrganization, LocalDate acquisitionAt) {
        this.id = id;
        this.user = user;
        this.certificationImages = certificationImages;
        this.approval = approval;
        this.name = name;
        this.issuingOrganization = issuingOrganization;
        this.acquisitionAt = acquisitionAt;
    }

    public List<CertificationImage> saveImages(List<String> certificationImgUrls) {
        return this.certificationImages = certificationImgUrls.stream()
                .map(CertificationImage::of)
                .map((certificationImage -> certificationImage.addCertification(this)))
                .toList();
    }

    public void addApproval(Approval approval) {
        this.approval = approval;
    }

    public void addCertificationImageUrls(List<String> certificationImageUrls) {
        this.certificationImages = certificationImageUrls.stream()
                .map(CertificationImage::of)
                .toList();

        certificationImages.stream().forEach(awardImage -> awardImage.addCertification(this));
    }
}
