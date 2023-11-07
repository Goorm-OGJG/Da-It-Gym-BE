package com.ogjg.daitgym.journal.dto.response.dto;

import com.ogjg.daitgym.domain.journal.ExerciseList;
import com.ogjg.daitgym.domain.TimeTemplate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserJournalDetailExerciseListDto {

    private Long exerciseListId;
    private final boolean isSpread = false;
    private String exerciseName;
    private String part;
    private int exerciseNum;
    private TimeTemplate restTime;
    private List<UserJournalDetailExerciseHistoryDto> exerciseSets = new ArrayList<>();

    public UserJournalDetailExerciseListDto(
            ExerciseList exerciseList, String part,
            List<UserJournalDetailExerciseHistoryDto> exerciseSets
    ) {
        this.exerciseListId = exerciseList.getId();
        this.exerciseName = exerciseList.getExercise().getName();
        this.part = part;
        this.exerciseNum = exerciseList.getExerciseNum();
        this.restTime = exerciseList.getRestTime();
        this.exerciseSets = exerciseSets;
    }
}
