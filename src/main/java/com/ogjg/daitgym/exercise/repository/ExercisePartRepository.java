package com.ogjg.daitgym.exercise.repository;

import com.ogjg.daitgym.domain.exercise.Exercise;
import com.ogjg.daitgym.domain.exercise.ExercisePart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExercisePartRepository extends JpaRepository<ExercisePart, Long> {

    Optional<ExercisePart> findByExercise(Exercise exercise);

    @EntityGraph(attributePaths = {"exercise"})
    List<ExercisePart> findAllByPart(String part);

}
