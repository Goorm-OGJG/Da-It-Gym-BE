package com.ogjg.daitgym.exercise.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ExerciseListResponse {

    List<ExerciseListDto> exercises;

    public ExerciseListResponse(List<ExerciseListDto> exercises) {
        this.exercises = exercises;
    }
}
