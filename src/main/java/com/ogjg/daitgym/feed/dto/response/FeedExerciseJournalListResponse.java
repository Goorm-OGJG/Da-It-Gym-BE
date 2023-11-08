package com.ogjg.daitgym.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalListResponse {

    private Long feedJournalId;
    private int likes;
    private int scrapCounts;
    private String image;

    public FeedExerciseJournalListResponse(
            Long feedJournalId, int likes, int scrapCounts, String image
    ) {
        this.feedJournalId = feedJournalId;
        this.likes = likes;
        this.scrapCounts = scrapCounts;
        this.image = image;
    }
}
