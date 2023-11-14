package com.ogjg.daitgym.feed.repository;

import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedExerciseJournalCollectionRepository extends JpaRepository<FeedExerciseJournalCollection, Long> {

    int countByPkFeedExerciseJournalId(Long feedJournalId);

    void deleteAllByFeedExerciseJournal(FeedExerciseJournal feedExerciseJournal);

    Optional<FeedExerciseJournalCollection> findByUserAndFeedExerciseJournal(User user,FeedExerciseJournal feedExerciseJournal);

}
