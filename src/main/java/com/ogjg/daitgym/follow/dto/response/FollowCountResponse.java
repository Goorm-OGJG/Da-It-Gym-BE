package com.ogjg.daitgym.follow.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FollowCountResponse {

    private int followCounts;

    public FollowCountResponse(int followCount) {
        this.followCounts = followCount;
    }
}
