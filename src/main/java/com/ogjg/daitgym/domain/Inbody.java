package com.ogjg.daitgym.domain;

import com.ogjg.daitgym.domain.routine.Routine;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Inbody extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    private int score;

    private double skeletalMuscleMass;

    private double bodyFatRatio;

    private double weight;

    private int basalMetabolicRate;

    private LocalDate measureAt;

    @ManyToOne(fetch = LAZY)
    private Routine routine;

    @Builder
    public Inbody(Long id, User user, int score, double skeletalMuscleMass, double bodyFatRatio, double weight, int basalMetabolicRate, LocalDate measureAt, Routine routine) {
        this.id = id;
        this.user = user;
        this.score = score;
        this.skeletalMuscleMass = skeletalMuscleMass;
        this.bodyFatRatio = bodyFatRatio;
        this.weight = weight;
        this.basalMetabolicRate = basalMetabolicRate;
        this.measureAt = measureAt;
        this.routine = routine;
    }
}
