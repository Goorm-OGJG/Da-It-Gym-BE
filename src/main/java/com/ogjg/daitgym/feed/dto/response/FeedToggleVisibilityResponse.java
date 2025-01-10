package com.ogjg.daitgym.feed.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedToggleVisibilityResponse {

    boolean isVisible;

    public FeedToggleVisibilityResponse(boolean isVisible) {
        this.isVisible = isVisible;
    }
}