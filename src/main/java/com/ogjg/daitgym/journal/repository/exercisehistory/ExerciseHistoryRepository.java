package com.ogjg.daitgym.journal.repository.exercisehistory;

import com.ogjg.daitgym.domain.journal.ExerciseHistory;
import com.ogjg.daitgym.domain.journal.ExerciseList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseHistoryRepository extends JpaRepository<ExerciseHistory, Long>, ExerciseHistoryRepositoryCustom {

    List<ExerciseHistory> findAllByExerciseList(ExerciseList exerciseList);

    void deleteAllByExerciseList(ExerciseList exerciseList);

}
