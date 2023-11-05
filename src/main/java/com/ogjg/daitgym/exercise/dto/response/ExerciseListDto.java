package com.ogjg.daitgym.exercise.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ExerciseListDto {

    private Long exerciseId;
    private String exerciseName;
    private String exercisePart;

    public ExerciseListDto(Long exerciseId, String exerciseName, String exercisePart) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.exercisePart = exercisePart;
    }
}
