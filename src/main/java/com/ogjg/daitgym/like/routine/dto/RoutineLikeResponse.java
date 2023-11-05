package com.ogjg.daitgym.like.routine.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class RoutineLikeResponse {
    private int likeCnt;

    public RoutineLikeResponse(int likeCnt) {
        this.likeCnt = likeCnt;
    }
}
