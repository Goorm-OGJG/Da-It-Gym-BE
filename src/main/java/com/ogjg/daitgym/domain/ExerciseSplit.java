package com.ogjg.daitgym.domain;

import com.ogjg.daitgym.user.exception.NotFoundSplitTitle;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ExerciseSplit {
    ONE_DAY("무분할"),
    TWO_DAY("2분할"),
    THREE_DAY("3분할"),
    FOUR_DAY("4분할"),
    FIVE_DAY("5분할"),
    OVER_SIX_DAY("6분할+"),
    ;

    private String title;

    ExerciseSplit(String title) {
        this.title = title;
    }

    public static ExerciseSplit titleFrom(String split) {
        return Arrays.stream(ExerciseSplit.values())
                .filter((exerciseSplit) -> exerciseSplit.getTitle().equals(split))
                .findAny()
                .orElseThrow(() -> new NotFoundSplitTitle());
    }
}
