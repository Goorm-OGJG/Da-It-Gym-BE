package com.ogjg.daitgym.comment.routine.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CreateRoutineCommentResponse {
    private Long parentId;
    private Long id;
    private String comment;
    private String nickname;

    @Builder
    public CreateRoutineCommentResponse(Long parentId, Long id, String comment, String nickname) {
        this.parentId = parentId;
        this.id = id;
        this.comment = comment;
        this.nickname = nickname;
    }
}
