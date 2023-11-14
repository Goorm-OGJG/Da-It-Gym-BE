package com.ogjg.daitgym.routine.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoutineDto {

    private Long id;
    private String title;
    private String author;
    private String authorImg;
    private String description;
    private boolean liked;
    private long likeCounts;
    private long scrapCounts;
    private LocalDateTime createdAt;

    @Builder

    public RoutineDto(Long id, String title, String author, String authorImg, String description, boolean liked, long likeCounts, long scrapCounts, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorImg = authorImg;
        this.description = description;
        this.liked = liked;
        this.likeCounts = likeCounts;
        this.scrapCounts = scrapCounts;
        this.createdAt = createdAt;
    }
}
