package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Certificate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @OneToMany(mappedBy = "certificate")
    private List<CertificateImage> certificateImages = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "approval_id")
    private Approval approval;

    private String name;

    private String issuingOrganization;

    private LocalDate acquisitionAt;
}
