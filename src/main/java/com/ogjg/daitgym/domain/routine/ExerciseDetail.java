package com.ogjg.daitgym.domain.routine;

import com.ogjg.daitgym.domain.BaseEntity;
import com.ogjg.daitgym.domain.exercise.Exercise;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ExerciseDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "day_id")
    private Day day;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private int setCount;

    private int repetitionCount;

    private int weight;

    private int exerciseOrder;

    private LocalTime restTime;

    @Builder
    public ExerciseDetail(Day day, Exercise exercise, int setCount, int repetitionCount, int weight, int exerciseOrder, LocalTime restTime) {
        this.day = day;
        this.exercise = exercise;
        this.setCount = setCount;
        this.repetitionCount = repetitionCount;
        this.weight = weight;
        this.exerciseOrder = exerciseOrder;
        this.restTime = restTime;
    }
}
