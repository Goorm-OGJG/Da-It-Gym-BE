package com.ogjg.daitgym.journal.repository.journal;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ogjg.daitgym.domain.journal.QExerciseHistory.exerciseHistory;
import static com.ogjg.daitgym.domain.journal.QExerciseJournal.exerciseJournal;
import static com.ogjg.daitgym.domain.journal.QExerciseList.exerciseList;

@RequiredArgsConstructor
public class ExerciseJournalRepositoryImpl implements ExerciseJournalRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 운동일지 관련 내용 fetch join으로 한번에 검색하기 상세보기
     */
    public List<Tuple> fetchCompleteExerciseJournalByJournalId(
            Long journalId
    ) {
        return jpaQueryFactory.select(
                        exerciseJournal, exerciseList, exerciseHistory
                ).from(exerciseJournal)
                .where(exerciseJournal.id.eq(journalId))
                .join(exerciseList).on(exerciseJournal.id.eq(exerciseList.exerciseJournal.id)).fetchJoin()
                .join(exerciseHistory).on(exerciseList.id.eq(exerciseHistory.exerciseList.id)).fetchJoin()
                .fetch();
    }
}
