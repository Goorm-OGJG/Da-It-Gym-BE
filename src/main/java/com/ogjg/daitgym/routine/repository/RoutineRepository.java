package com.ogjg.daitgym.routine.repository;

import com.ogjg.daitgym.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineRepository extends JpaRepository<Routine, String> {
}
