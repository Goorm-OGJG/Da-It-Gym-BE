package com.ogjg.daitgym.feed.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Setter
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedSearchConditionRequest {

    private String split;
    private List<String> part = new ArrayList<>();
}
