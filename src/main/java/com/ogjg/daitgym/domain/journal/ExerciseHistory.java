package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.journal.dto.request.ExerciseHistoryRequest;
import com.ogjg.daitgym.journal.dto.request.UpdateExerciseHistoryRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExerciseHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exercise_history_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exercise_list_id")
    private ExerciseList exerciseList;

    private int setNum;

    private int weight;

    private int repetitionCount;

    private boolean isCompleted = false;

    public static ExerciseHistory createExerciseHistory(ExerciseList exerciseList, ExerciseHistoryRequest exerciseHistoryRequest) {
        return builder()
                .exerciseList(exerciseList)
                .setNum(exerciseHistoryRequest.getSetNum())
                .weight(exerciseHistoryRequest.getWeights())
                .repetitionCount(exerciseHistoryRequest.getCounts())
                .build();
    }

    public void updateHistory(
            UpdateExerciseHistoryRequest updateExerciseHistoryRequest
    ) {
        this.weight = updateExerciseHistoryRequest.getWeight();
        this.repetitionCount = updateExerciseHistoryRequest.getCount();
        this.isCompleted = updateExerciseHistoryRequest.isCompleted();
    }

    @Builder
    public ExerciseHistory(
            ExerciseList exerciseList, int setNum, int weight,
            int repetitionCount, boolean isCompleted
    ) {
        this.exerciseList = exerciseList;
        this.setNum = setNum;
        this.weight = weight;
        this.repetitionCount = repetitionCount;
        this.isCompleted = isCompleted;
    }
}
