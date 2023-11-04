package com.ogjg.daitgym.comment.routine.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EditRoutineCommentRequest {
    private String comment;

}
