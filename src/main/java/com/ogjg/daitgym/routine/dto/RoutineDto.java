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
    private int division;
    private boolean liked;
    private long likeCounts;
    private boolean scrapped;
    private long scrapCounts;
    private LocalDateTime createdAt;

    @Builder
    public RoutineDto(Long id, String title, String author, String authorImg, String description, int division, boolean liked, long likeCounts, boolean scrapped, long scrapCounts, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorImg = authorImg;
        this.description = description;
        this.division = division;
        this.liked = liked;
        this.likeCounts = likeCounts;
        this.scrapped = scrapped;
        this.scrapCounts = scrapCounts;
        this.createdAt = createdAt;
    }
}
