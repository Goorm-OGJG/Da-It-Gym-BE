package com.ogjg.daitgym.feed.repository;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedExerciseJournalRepository extends JpaRepository<FeedExerciseJournal, Long> {

    Optional<FeedExerciseJournal> findByExerciseJournal(ExerciseJournal exerciseJournal);

}
