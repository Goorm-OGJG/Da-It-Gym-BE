package com.ogjg.daitgym.exercise.service;

import com.ogjg.daitgym.common.exception.exercise.NotFoundExercise;
import com.ogjg.daitgym.common.exception.exercise.NotFoundExercisePart;
import com.ogjg.daitgym.domain.exercise.Exercise;
import com.ogjg.daitgym.domain.exercise.ExercisePart;
import com.ogjg.daitgym.exercise.repository.ExercisePartRepository;
import com.ogjg.daitgym.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseHelper {

    private final ExerciseRepository exerciseRepository;
    private final ExercisePartRepository exercisePartRepository;

    /**
     * 운동검색
     * 운동이름으로 운동 존재하는지 확인하기
     */
    public Exercise findExercise(String exerciseName) {
        return exerciseRepository.findByName(exerciseName)
                .orElseThrow(NotFoundExercise::new);
    }

    /**
     * 운동검색
     * 운동Id로 운동 존재하는지 확인하기
     */
    public Exercise findExercise(Long exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(NotFoundExercise::new);
    }

    /**
     * 운동으로 운동부위 찾기
     */
    public String findExercisePartByExercise(
            Exercise exercise
    ) {
        return exercisePartRepository.findByExercise(exercise)
                .orElseThrow(NotFoundExercisePart::new)
                .getPart();
    }

    /**
     * 운동부위로 운동 찾기
     */
    public List<ExercisePart> findExerciseListsByPart(
            String part
    ) {
        return exercisePartRepository.findAllByPart(part);
    }

}
