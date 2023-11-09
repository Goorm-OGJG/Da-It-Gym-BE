package com.ogjg.daitgym.routine.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class RoutineDto {

    private Long id;
    private String title;
    private String author;
    private String description;
    private boolean liked;
    private int likeCounts;
    private int scrapCounts;
    private LocalDateTime createdAt;

    @Builder
    public RoutineDto(Long id, String title, String author, String description, boolean liked, int likeCounts, int scrapCounts, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.liked = liked;
        this.likeCounts = likeCounts;
        this.scrapCounts = scrapCounts;
        this.createdAt = createdAt;
    }
}
