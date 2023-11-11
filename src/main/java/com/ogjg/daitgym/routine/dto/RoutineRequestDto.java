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
    public class RoutineDto {
        private Long id;
        private List<DayDto> days;
    }

    @Getter
    public class DayDto {
        private Long id;
        private int order;
        private long dayDate;
        private ExerciseTimeDto exerciseTime;
        private boolean completed;
        private boolean spread;
        private List<ExerciseDto> exercises;
    }

    @Getter
    public class ExerciseDto {
        private Long id;
        private int order;
        private String name;
        private String part;
        private RestTimeDto restTime;
        private boolean spread;
        private List<ExerciseSetDto> exerciseSets;
    }

    @Getter
    public class ExerciseSetDto {
        private Long id;
        private int order;
        private int weights;
        private int counts;
        private boolean completed;
    }

    @Getter
    public class RestTimeDto {
        private int hours;
        private int minutes;
        private int seconds;
    }

    @Getter
    public class ExerciseTimeDto {
        private int hours;
        private int minutes;
        private int seconds;
    }
}

