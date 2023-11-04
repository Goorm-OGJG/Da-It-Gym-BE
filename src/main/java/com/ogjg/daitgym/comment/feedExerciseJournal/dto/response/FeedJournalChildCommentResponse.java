package com.ogjg.daitgym.comment.feedExerciseJournal.dto.response;

import com.ogjg.daitgym.comment.feedExerciseJournal.dto.FeedJournalChildDto;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class FeedJournalChildCommentResponse {

    private int childCommentsCnt;
    private boolean authority;
    private List<FeedJournalChildDto> childComments;


    public FeedJournalChildCommentResponse(int childCommentsCnt, boolean authority, List<FeedExerciseJournalComment> feedExerciseJournalComments) {
        this.childCommentsCnt = childCommentsCnt;
        this.authority = authority;

        List<FeedExerciseJournalComment> sortedComments = new ArrayList<>(feedExerciseJournalComments);
        sortedComments.sort(Comparator.comparing(FeedExerciseJournalComment::getCreatedAt).reversed());
        this.childComments = sortedComments.stream().map(FeedJournalChildDto::new).collect(Collectors.toList());
    }
}
