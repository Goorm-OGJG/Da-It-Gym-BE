package com.ogjg.daitgym.journal.dto.response.dto;

import com.ogjg.daitgym.domain.journal.ExerciseHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserJournalDetailExerciseHistoryDto {

    private Long exerciseHistoryId;
    private int setNum;
    private int weight;
    private int count;
    private boolean isCompleted;

    public UserJournalDetailExerciseHistoryDto(ExerciseHistory exerciseHistory) {
        this.exerciseHistoryId = exerciseHistory.getId();
        this.setNum = exerciseHistory.getSetNum();
        this.weight = exerciseHistory.getWeight();
        this.count = exerciseHistory.getRepetitionCount();
        this.isCompleted = exerciseHistory.isCompleted();
    }
}
