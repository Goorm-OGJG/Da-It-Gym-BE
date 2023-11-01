package com.ogjg.daitgym.journal.repository.exercise;

import com.ogjg.daitgym.domain.Exercise;
import com.ogjg.daitgym.domain.ExercisePart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExercisePartRepository extends JpaRepository<ExercisePart, Long> {

    Optional<ExercisePart> findByExercise(Exercise exercise);

}
