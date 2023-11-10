package com.ogjg.daitgym.comment.routine.dto;

import com.ogjg.daitgym.domain.routine.RoutineComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoutineCommentDto {
    private long commentId;
    private String nickname;
    private String imageUrl;
    private String comment;
    private int childrenCnt;

    public RoutineCommentDto(RoutineComment routineComment) {
        this.commentId = routineComment.getId();
        this.nickname = routineComment.getUser().getNickname();
        this.imageUrl = routineComment.getUser().getImageUrl();
        this.comment = routineComment.getComment();
        this.childrenCnt = routineComment.getChildren().size();
    }
}
