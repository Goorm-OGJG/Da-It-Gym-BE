package com.ogjg.daitgym.journal.repository.exercise;

import com.ogjg.daitgym.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);
}
