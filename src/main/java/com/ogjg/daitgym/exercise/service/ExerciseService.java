package com.ogjg.daitgym.exercise.service;

import com.ogjg.daitgym.exercise.dto.response.ExerciseListDto;
import com.ogjg.daitgym.exercise.dto.response.ExerciseListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseHelper exerciseHelper;

    /**
     * 운동부위에 속하는 운동 목록 반환
     */
    public ExerciseListResponse exerciseLists(
            String part
    ) {
        List<ExerciseListDto> exerciseListDtos = exerciseHelper.findExerciseListsByPart(part)
                .stream()
                .map(exercisePart -> new ExerciseListDto(
                        exercisePart.getExercise().getId(),
                        exercisePart.getExercise().getName(),
                        exercisePart.getPart())
                ).toList();

        return new ExerciseListResponse(exerciseListDtos);
    }
}
