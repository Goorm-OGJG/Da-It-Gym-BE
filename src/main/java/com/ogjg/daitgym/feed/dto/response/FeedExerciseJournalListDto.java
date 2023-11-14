package com.ogjg.daitgym.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalListDto {

    private Long id;
    private int likes;
    private int scrapCounts;
    private String image;

    public FeedExerciseJournalListDto(
            Long feedJournalId, int likes, int scrapCounts, String image
    ) {
        this.id = feedJournalId;
        this.likes = likes;
        this.scrapCounts = scrapCounts;
        this.image = image;
    }
}
