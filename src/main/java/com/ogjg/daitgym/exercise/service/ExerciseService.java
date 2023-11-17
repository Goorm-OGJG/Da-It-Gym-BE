package com.ogjg.daitgym.exercise.service;

import com.ogjg.daitgym.domain.exercise.Exercise;
import com.ogjg.daitgym.domain.exercise.ExercisePart;
import com.ogjg.daitgym.exercise.dto.response.ExerciseListDto;
import com.ogjg.daitgym.exercise.dto.response.ExerciseListResponse;
import com.ogjg.daitgym.common.exception.exercise.NotFoundExercise;
import com.ogjg.daitgym.common.exception.exercise.NotFoundExercisePart;
import com.ogjg.daitgym.exercise.repository.ExercisePartRepository;
import com.ogjg.daitgym.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExercisePartRepository exercisePartRepository;

    /**
     * 운동검색
     * 운동이름으로 운동 존재하는지 확인하기
     */
    @Transactional(readOnly = true)
    public Exercise findExercise(String exerciseName) {
        return exerciseRepository.findByName(exerciseName)
                .orElseThrow(NotFoundExercise::new);
    }

    /**
     * 운동으로 운동부위 찾기
     */
    @Transactional(readOnly = true)
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
    private List<ExercisePart> findExerciseListsByPart(
            String part
    ) {
        return exercisePartRepository.findAllByPart(part);
    }

    /**
     * 운동부위에 속하는 운동 목록 반환
     */
    public ExerciseListResponse exerciseLists(
            String part
    ) {
        List<ExerciseListDto> exerciseListDtos = findExerciseListsByPart(part)
                .stream()
                .map(
                        exercisePart -> new ExerciseListDto(
                                exercisePart.getExercise().getId(),
                                exercisePart.getExercise().getName(),
                                exercisePart.getPart()
                        )
                ).toList();

        return new ExerciseListResponse(exerciseListDtos);
    }
}
