package com.ogjg.daitgym.comment.routine.dto;

import com.ogjg.daitgym.domain.RoutineComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoutineChildDto {
    private long childCommentId;
    private String nickname;
    private String imageUrl;
    private String childComment;

    public RoutineChildDto(RoutineComment routineComment) {
        this.childCommentId = routineComment.getId();
        this.nickname = routineComment.getUser().getNickname();
        this.imageUrl = routineComment.getUser().getImageUrl();
        this.childComment = routineComment.getComment();
    }
}
