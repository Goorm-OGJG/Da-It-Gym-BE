package com.ogjg.daitgym.comment.feedExerciseJournal.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EditFeedJournalCommentRequest {
    private Long parentId;
    private String comment;
}
