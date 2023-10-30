package com.ogjg.daitgym.journal.repository.journal;

import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseJournalRepository extends JpaRepository<ExerciseJournal, Long>, ExerciseJournalRepositoryCustom {


}
