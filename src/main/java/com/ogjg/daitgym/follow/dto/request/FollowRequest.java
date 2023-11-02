package com.ogjg.daitgym.follow.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FollowRequest {

    private String email;

}
