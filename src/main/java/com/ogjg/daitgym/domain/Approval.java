package com.ogjg.daitgym.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
}
