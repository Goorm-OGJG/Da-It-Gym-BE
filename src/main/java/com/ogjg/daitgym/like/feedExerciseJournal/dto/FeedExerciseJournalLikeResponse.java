package com.ogjg.daitgym.like.feedExerciseJournal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalLikeResponse {
    private int likeCnt;

    public FeedExerciseJournalLikeResponse(int likeCnt) {
        this.likeCnt = likeCnt;
    }
}
