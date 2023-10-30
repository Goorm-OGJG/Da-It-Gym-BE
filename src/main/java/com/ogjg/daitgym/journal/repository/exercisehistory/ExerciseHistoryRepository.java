package com.ogjg.daitgym.journal.repository.exercisehistory;

import com.ogjg.daitgym.domain.journal.ExerciseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseHistoryRepository extends JpaRepository<ExerciseHistory, Long>, ExerciseHistoryRepositoryCustom {
}
