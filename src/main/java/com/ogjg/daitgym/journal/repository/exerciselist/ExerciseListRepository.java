package com.ogjg.daitgym.journal.repository.exerciselist;

import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseListRepository extends JpaRepository<ExerciseList, Long> {

    List<ExerciseList> findByExerciseJournal(ExerciseJournal exerciseJournal);

    void deleteAllByExerciseJournal(ExerciseJournal exerciseJournal);

    Optional<List<ExerciseList>> findByExerciseJournalId(Long exerciseJournalId);
}
