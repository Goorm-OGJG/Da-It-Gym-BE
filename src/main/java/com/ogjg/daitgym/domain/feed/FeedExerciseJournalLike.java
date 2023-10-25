package com.ogjg.daitgym.domain.feed;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.User;
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
public class FeedExerciseJournalLike extends BaseEntity {

    @EmbeddedId
    private ExerciseJournalLikePk exerciseJournalLikePk;

    @MapsId("email")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @MapsId("exerciseJournalId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "journal_id")
    private FeedExerciseJournal feedExerciseJournal;

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