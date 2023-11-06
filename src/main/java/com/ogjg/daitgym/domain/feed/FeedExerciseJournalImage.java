package com.ogjg.daitgym.domain.feed;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalImage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "feed_journal_image")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "feed_journal_id")
    private FeedExerciseJournal feedExerciseJournal;

    private String imageUrl;

    public FeedExerciseJournalImage(FeedExerciseJournal feedExerciseJournal, String imageUrl) {
        this.feedExerciseJournal = feedExerciseJournal;
        this.imageUrl = imageUrl;
    }
}
