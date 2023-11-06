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
public class CertificationImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    private String url;

    @Builder
    public CertificationImage(Long id, Certification certification, String url) {
        this.id = id;
        this.certification = certification;
        this.url = url;
    }
}
