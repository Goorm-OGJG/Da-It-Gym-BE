package com.ogjg.daitgym.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedExerciseJournalListResponse {

    private int totalPage;
    private List<FeedExerciseJournalListDto> feedExerciseJournalLists = new ArrayList<>();

    public FeedExerciseJournalListResponse(int totalPage, List<FeedExerciseJournalListDto> feedExerciseJournalLists) {
        this.totalPage = totalPage;
        this.feedExerciseJournalLists = feedExerciseJournalLists;
    }
}
