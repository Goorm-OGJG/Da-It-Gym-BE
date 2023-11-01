package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.Exercise;
import com.ogjg.daitgym.journal.dto.request.ExerciseListRequest;
import com.ogjg.daitgym.journal.dto.request.UpdateRestTimeRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExerciseList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exercise_list_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "journal_id")
    private ExerciseJournal exerciseJournal;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private int exerciseNum;

    @Embedded
    private TimeTemplate restTime;

    public static ExerciseList createExercise(
            ExerciseJournal exerciseJournal,
            Exercise exercise,
            ExerciseListRequest exerciseListRequest
    ) {
        return builder()
                .exerciseJournal(exerciseJournal)
                .exercise(exercise)
                .exerciseNum(exerciseListRequest.getExerciseNum())
                .restTime(new TimeTemplate(exerciseListRequest.getRestTime()))
                .build();
    }

    public void changeRestTime(
            UpdateRestTimeRequest updateRestTimeRequest
    ) {
        this.restTime = new TimeTemplate(updateRestTimeRequest.getRestTime());
    }

    @Builder
    public ExerciseList(ExerciseJournal exerciseJournal, Exercise exercise, int exerciseNum, TimeTemplate restTime) {
        this.exerciseJournal = exerciseJournal;
        this.exercise = exercise;
        this.exerciseNum = exerciseNum;
        this.restTime = restTime;
    }
}
