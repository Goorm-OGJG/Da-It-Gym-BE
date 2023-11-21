package com.ogjg.daitgym.routine.repository;

import com.ogjg.daitgym.journal.dto.request.ReplicationRoutineDto;

import java.util.List;

public interface RoutineRepositoryCustom {

    List<ReplicationRoutineDto> getOriginalRoutinesToReplicate(Long dayId);

    List<ReplicationRoutineDto> getOriginalRoutinesToReplicateExerciseLists(Long dayId);

    List<ReplicationRoutineDto> getOriginalRoutinesToReplicateExerciseHistories(Long dayId, Long exerciseId);

}
