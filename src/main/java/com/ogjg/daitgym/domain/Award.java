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
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Award extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @OneToMany(mappedBy = "award", cascade = ALL, orphanRemoval = true)
    private List<AwardImage> awardImages = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "approval_id")
    private Approval approval;

    private String competitionName;

    private String hostOrganization;

    private int ranking;

    private String awardName;

    private LocalDate awardAt;

    @Builder
    public Award(Long id, User user, List<AwardImage> awardImages, Approval approval, String competitionName, String hostOrganization, int ranking, String awardName, LocalDate awardAt) {
        this.id = id;
        this.user = user;
        this.awardImages = awardImages;
        this.approval = approval;
        this.competitionName = competitionName;
        this.hostOrganization = hostOrganization;
        this.ranking = ranking;
        this.awardName = awardName;
        this.awardAt = awardAt;
    }

    public List<AwardImage> saveImages(List<String> awardImgUrls) {
        return this.awardImages = awardImgUrls.stream()
                .map(AwardImage::of)
                .map((awardImage -> awardImage.addAward(this)))
                .toList();
    }

    public void addApproval(Approval approval) {
        this.approval = approval;
    }

    public void addImageUrls(List<String> awardImageUrls) {
        this.awardImages = awardImageUrls.stream()
                .map(AwardImage::of)
                .toList();

        awardImages.stream().forEach(awardImage -> awardImage.addAward(this));
    }
}
