package com.ogjg.daitgym.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalCountResponse {

    private int count;

    public FeedExerciseJournalCountResponse(int count) {
        this.count = count;
    }
}
