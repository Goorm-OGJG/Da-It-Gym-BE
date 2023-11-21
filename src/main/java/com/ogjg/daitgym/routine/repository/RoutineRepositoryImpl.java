package com.ogjg.daitgym.routine.repository;

import com.ogjg.daitgym.journal.dto.request.QReplicationRoutineDto;
import com.ogjg.daitgym.journal.dto.request.ReplicationRoutineDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ogjg.daitgym.domain.routine.QDay.day;
import static com.ogjg.daitgym.domain.routine.QExerciseDetail.exerciseDetail;
import static com.ogjg.daitgym.domain.routine.QRoutine.routine;

@RequiredArgsConstructor
public class RoutineRepositoryImpl implements RoutineRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ReplicationRoutineDto> getOriginalRoutinesToReplicate(
            Long dayId
    ) {
        return jpaQueryFactory.select(
                        new QReplicationRoutineDto(
                                routine.division,
                                exerciseDetail.restTime,
                                exerciseDetail.exerciseOrder,
                                exerciseDetail.setOrder,
                                exerciseDetail.repetitionCount,
                                exerciseDetail.weight,
                                exerciseDetail.exercise.id
                        )).from(day)
                .where(day.id.eq(dayId))
                .join(day.routine, routine).fetchJoin()
                .join(day.exerciseDetails, exerciseDetail).fetchJoin()
                .fetch();
    }

    public List<ReplicationRoutineDto> getOriginalRoutinesToReplicateExerciseLists(
            Long dayId
    ) {
        return jpaQueryFactory.select(
                        new QReplicationRoutineDto(
                                exerciseDetail.restTime,
                                exerciseDetail.exerciseOrder,
                                exerciseDetail.exercise.id
                        )).from(day)
                .where(day.id.eq(dayId))
                .join(day.exerciseDetails, exerciseDetail)
                .distinct()
                .fetch();
    }

    public List<ReplicationRoutineDto> getOriginalRoutinesToReplicateExerciseHistories(
            Long dayId, Long exerciseId
    ) {
        return jpaQueryFactory.select(
                        new QReplicationRoutineDto(
                                exerciseDetail.setOrder,
                                exerciseDetail.repetitionCount,
                                exerciseDetail.weight
                        )).from(day)
                .where(day.id.eq(dayId))
                .join(day.exerciseDetails, exerciseDetail)
                .where(exerciseDetail.exercise.id.eq(exerciseId))
                .fetch();
    }

}