package com.ogjg.daitgym.domain.journal;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.TimeTemplate;
import com.ogjg.daitgym.domain.exercise.Exercise;
import com.ogjg.daitgym.journal.dto.request.ExerciseListRequest;
import com.ogjg.daitgym.journal.dto.request.ReplicationRoutineDto;
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

    public static ExerciseList createExerciseList(
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

    //todo 중복되는코드 dto로 받지않고 그냥 각각의 변수로 받으면 중복코드를 없앨수있을것같다
    public static ExerciseList replicateExerciseListByJournal(
            ExerciseJournal replicatedExerciseJournal,
            ExerciseList originalExerciseList
    ) {
        return builder()
                .exerciseJournal(replicatedExerciseJournal)
                .exercise(originalExerciseList.exercise)
                .exerciseNum(originalExerciseList.getExerciseNum())
                .restTime(new TimeTemplate(originalExerciseList.restTime))
                .build();
    }

    public static ExerciseList replicateExerciseListByRoutine(
            ExerciseJournal replicatedExerciseJournal,
            ReplicationRoutineDto replicationRoutine,
            Exercise exercise
    ) {
        return builder()
                .exerciseJournal(replicatedExerciseJournal)
                .exercise(exercise)
                .exerciseNum(replicationRoutine.getExerciseListNum())
                .restTime(new TimeTemplate(replicationRoutine.getRestTime()))
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
