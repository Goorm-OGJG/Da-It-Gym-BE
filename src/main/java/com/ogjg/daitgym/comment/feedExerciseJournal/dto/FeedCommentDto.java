package com.ogjg.daitgym.comment.feedExerciseJournal.dto;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournalComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedCommentDto {
    private long commentId;
    private String nickname;
    private String imageUrl;
    private String comment;
    private int childrenCnt;

    public FeedCommentDto(FeedExerciseJournalComment comment) {
        this.commentId = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.imageUrl = comment.getUser().getImageUrl();
        this.comment = comment.getComment();
        this.childrenCnt = comment.getChildren().size();
    }
}
