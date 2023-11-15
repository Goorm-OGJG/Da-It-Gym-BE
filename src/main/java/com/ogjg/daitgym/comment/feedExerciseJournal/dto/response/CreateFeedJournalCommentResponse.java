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
    private String imageUrl;

    @Builder
    public CreateFeedJournalCommentResponse(Long id, String comment, String nickname, Long parentId, String imageUrl) {
        this.parentId = parentId;
        this.id = id;
        this.comment = comment;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
