package com.ogjg.daitgym.journal.repository.journal;

import com.querydsl.core.Tuple;

import java.util.List;

public interface ExerciseJournalRepositoryCustom {

    List<Tuple> fetchCompleteExerciseJournalByJournalId(Long journalId);
}
