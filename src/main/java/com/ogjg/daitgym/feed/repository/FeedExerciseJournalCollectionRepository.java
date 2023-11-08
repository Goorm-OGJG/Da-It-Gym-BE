package com.ogjg.daitgym.feed.repository;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournalCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedExerciseJournalCollectionRepository extends JpaRepository<FeedExerciseJournalCollection, Long> {

    int countByPkFeedExerciseJournalId(Long feedJournalId);

}
