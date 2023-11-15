package com.ogjg.daitgym.routine.dto;

import com.ogjg.daitgym.domain.TimeTemplate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RoutineDetailsResponseDto {

    private String writer;
    private String writerImg;
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private boolean liked;
    private long likeCounts;
    private boolean scrapped;
    private long scrapCounts;
    private RoutineDto routine;

    @Builder
    public RoutineDetailsResponseDto(String writer, String writerImg, LocalDateTime createdAt, String title, String description, boolean liked, long likeCounts, boolean scrapped, long scrapCounts, RoutineDto routine) {
        this.writer = writer;
        this.writerImg = writerImg;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.liked = liked;
        this.likeCounts = likeCounts;
        this.scrapped = scrapped;
        this.scrapCounts = scrapCounts;
        this.routine = routine;
    }

    @Getter
    public static class RoutineDto {
        private Long id;
        private List<DayDto> days;

        @Builder
        public RoutineDto(Long id, List<DayDto> days) {
            this.id = id;
            this.days = days;
        }
    }

    @Getter
    public static class DayDto {
        private Long id;
        private int order;
        private boolean isSpread;
        private List<ExerciseDto> exercises;

        @Builder
        public DayDto(Long id, int order, boolean isSpread, List<ExerciseDto> exercises) {
            this.id = id;
            this.order = order;
            this.isSpread = isSpread;
            this.exercises = exercises;
        }
    }

    @Getter
    public static class ExerciseDto {
        private Long id;
        private int order;
        private String name;
        private String part;
        private RestTimeDto restTime;
        private List<ExerciseSets> exerciseSets;

        @Builder
        public ExerciseDto(Long id, int order, String name, String part, RestTimeDto restTime, List<ExerciseSets> exerciseSets) {
            this.id = id;
            this.order = order;
            this.name = name;
            this.part = part;
            this.restTime = restTime;
            this.exerciseSets = exerciseSets;
        }
    }

    @Getter
    public static class RestTimeDto {
        private int hours;
        private int minutes;
        private int seconds;

        @Builder
        public RestTimeDto(int hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }
    }

    @Getter
    public static class ExerciseSets {
        private Long id;
        private int order;
        private int weights;
        private int counts;
        private TimeTemplate restTime;
        private boolean completed;

        @Builder
        public ExerciseSets(Long id, int order, int weights, int counts, TimeTemplate restTime, boolean completed) {
            this.id = id;
            this.order = order;
            this.weights = weights;
            this.counts = counts;
            this.restTime = restTime;
            this.completed = completed;
        }
    }
}
