package com.ogjg.daitgym.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalCountResponse {

    private int counts;

    public FeedExerciseJournalCountResponse(int counts) {
        this.counts = counts;
    }
}
