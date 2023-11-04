package com.ogjg.daitgym.comment.feedExerciseJournal.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CreateFeedJournalCommentResponse {

    private Long parentId;
    private Long id;
    private String comment;
    private String nickname;

    @Builder
    public CreateFeedJournalCommentResponse(Long id, String comment, String nickname, Long parentId) {
        this.parentId = parentId;
        this.id = id;
        this.comment = comment;
        this.nickname = nickname;
    }
}
