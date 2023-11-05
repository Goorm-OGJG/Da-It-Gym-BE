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
    private FeedExerciseJournalLikePk feedJournalLikePk;

    @MapsId("email")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "email")
    private User user;

    @MapsId("feedExerciseJournalId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "feed_journal_id")
    private FeedExerciseJournal feedExerciseJournal;

    @Getter
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class FeedExerciseJournalLikePk implements Serializable {

        private String email;
        private Long feedExerciseJournalId;

        public FeedExerciseJournalLikePk(String email, Long feedExerciseJournalId) {
            this.email = email;
            this.feedExerciseJournalId = feedExerciseJournalId;
        }
    }

    public FeedExerciseJournalLike(User user, FeedExerciseJournal feedExerciseJournal) {
        this.feedJournalLikePk = new FeedExerciseJournalLikePk(user.getEmail(), feedExerciseJournal.getId());
        this.user = user;
        this.feedExerciseJournal = feedExerciseJournal;
    }

}