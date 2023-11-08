package com.ogjg.daitgym.routine.repository;

import com.ogjg.daitgym.domain.routine.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day, Long> {

    @Query("SELECT d FROM Day d LEFT JOIN FETCH d.exerciseDetails WHERE d.routine.id = :routineId")
    Optional<List<Day>> findAllWithExerciseDetailsByRoutineId(@Param("routineId") Long routineId);

}
