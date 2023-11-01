package com.ogjg.daitgym.journal.repository.exerciselist;

import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseListRepository extends JpaRepository<ExerciseList, Long> {

    List<ExerciseList> findByExerciseJournal(ExerciseJournal exerciseJournal);

    void deleteAllByExerciseJournal(ExerciseJournal exerciseJournal);

}
