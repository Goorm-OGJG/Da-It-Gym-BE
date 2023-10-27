package com.ogjg.daitgym.domain.feed;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalCollection extends BaseEntity {

    @EmbeddedId
    private PK pk;

    @MapsId("email")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @MapsId("feedExerciseJournalId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "feed_exercise_journal_id")
    private FeedExerciseJournal feedExerciseJournal;

    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor(access = PROTECTED)
    public static class PK implements Serializable {
        private String email;
        private Long feedExerciseJournalId;

        public PK(String email, Long feedExerciseJournalId) {
            this.email = email;
            this.feedExerciseJournalId = feedExerciseJournalId;
        }
    }

}
