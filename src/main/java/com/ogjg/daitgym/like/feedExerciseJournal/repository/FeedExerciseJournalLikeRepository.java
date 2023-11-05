package com.ogjg.daitgym.like.feedExerciseJournal.repository;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournalLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedExerciseJournalLikeRepository extends JpaRepository<FeedExerciseJournalLike, Long> {
    int countByFeedJournalLikePkFeedExerciseJournalId(Long feedJournalId);

    boolean existsByUserEmailAndFeedExerciseJournalId(String email, Long feedJournalId);
}
