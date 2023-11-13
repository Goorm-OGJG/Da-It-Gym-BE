package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class AwardImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "award_id")
    private Award award;

    private String url;

    @Builder
    public AwardImage(Long id, Award award, String url) {
        this.id = id;
        this.award = award;
        this.url = url;
    }

    public static AwardImage of(String url) {
        return AwardImage.builder()
                .url(url)
                .build();
    }

    public AwardImage addAward(Award savedAward) {
        this.award = savedAward;
        return this;
    }
}
