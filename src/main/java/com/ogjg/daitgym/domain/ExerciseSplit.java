package com.ogjg.daitgym.domain;

public enum ExerciseSplit {
    ONE_DAY("1분할"),
    TWO_DAY("2분할"),
    THREE_DAY("3분할"),
    FOUR_DAY("4분할"),
    FIVE_DAY("5분할"),
    OVER_SIX_DAY("6분할 이상"),
    ;

    private String title;

    ExerciseSplit(String title) {
        this.title = title;
    }
}
