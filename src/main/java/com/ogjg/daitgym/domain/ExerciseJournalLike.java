package com.ogjg.daitgym.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ExerciseJournalLike extends BaseEntity {

    @EmbeddedId
    private ExerciseJournalLikePk exerciseJournalLikePk;

    @MapsId("email")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    // TODO ExerciseJournal 연관관계 맺기

    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class ExerciseJournalLikePk implements Serializable {

        private String email;
        private Long exerciseJournalId;

        public ExerciseJournalLikePk(String email, Long exerciseJournalId) {
            this.email = email;
            this.exerciseJournalId = exerciseJournalId;
        }
    }
}