package com.ogjg.daitgym.follow.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FollowListResponse {

    List<FollowListDto> followList = new ArrayList<>();

    public FollowListResponse(List<FollowListDto> followList) {
        this.followList = followList;
    }
}
