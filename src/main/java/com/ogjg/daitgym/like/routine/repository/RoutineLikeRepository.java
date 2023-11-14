package com.ogjg.daitgym.like.routine.repository;

import com.ogjg.daitgym.domain.routine.RoutineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RoutineLikeRepository extends JpaRepository<RoutineLike, Long> {
    int countByRoutineLikePkRoutineId(Long routineId);

    boolean existsByUserEmailAndRoutineId(String email, Long RoutineId);

    @Query("SELECT rl.routine.id FROM RoutineLike rl WHERE rl.user.email = :email")
    Set<Long> findLikedRoutineIdByUserEmail(String email);

    @Query("SELECT rl.routine.id FROM RoutineLike rl WHERE rl.user.nickname = :nickname")
    Set<Long> findLikedRoutineIdByUserNickname(String nickname);

    @Query("SELECT COUNT(rl) FROM RoutineLike rl WHERE rl.routine.id = :routineId")
    long countByRoutineId(@Param("routineId") Long routineId);

}
