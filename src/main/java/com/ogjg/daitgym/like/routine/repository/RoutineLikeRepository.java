package com.ogjg.daitgym.like.routine.repository;

import com.ogjg.daitgym.domain.RoutineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoutineLikeRepository extends JpaRepository<RoutineLike, Long> {
    int countByRoutineLikePkRoutineId(Long routineId);

    boolean existsByUserEmailAndRoutineId(String email, Long RoutineId);

}
