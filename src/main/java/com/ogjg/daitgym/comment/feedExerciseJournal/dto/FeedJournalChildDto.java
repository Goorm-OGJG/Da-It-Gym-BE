package com.ogjg.daitgym.comment.feedExerciseJournal.dto;

import com.ogjg.daitgym.domain.feed.FeedExerciseJournalComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedJournalChildDto {
    private long childCommentId;
    private String nickname;
    private String imageUrl;
    private String childComment;

    public FeedJournalChildDto(FeedExerciseJournalComment comment) {
        this.childCommentId = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.imageUrl = comment.getUser().getImageUrl();
        this.childComment = comment.getComment();
    }
}
