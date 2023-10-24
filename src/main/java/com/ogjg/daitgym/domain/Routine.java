package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Routine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "original_routine")
    private Routine originalRoutine;

    private String title;

    private String content;

    private int duration;

    private UnitType unitType;
}
