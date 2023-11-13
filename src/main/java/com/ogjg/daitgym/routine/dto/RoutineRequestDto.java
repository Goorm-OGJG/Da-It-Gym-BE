package com.ogjg.daitgym.routine.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RoutineRequestDto {
    private String title;
    private String description;
    private int division;
    private RoutineDto routine;

    @Getter
    public static class RoutineDto {
        private Long id;
        private List<DayDto> days;
    }

    @Getter
    public static class DayDto {
        private Long id;
        private int order;
        private long dayDate;
        private ExerciseTimeDto exerciseTime;
        private boolean completed;
        private boolean spread;
        private List<ExerciseDto> exercises;
    }

    @Getter
    public static class ExerciseDto {
        private Long id;
        private int order;
        private String name;
        private String part;
        private RestTimeDto restTime;
        private boolean spread;
        private List<ExerciseSetDto> exerciseSets;
    }

    @Getter
    public static class ExerciseSetDto {
        private Long id;
        private int order;
        private int weights;
        private int counts;
        private boolean completed;
    }

    @Getter
    public static class RestTimeDto {
        private int hours;
        private int minutes;
        private int seconds;
    }

    @Getter
    public static class ExerciseTimeDto {
        private int hours;
        private int minutes;
        private int seconds;
    }
}

