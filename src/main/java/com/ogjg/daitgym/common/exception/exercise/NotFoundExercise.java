package com.ogjg.daitgym.common.exception.exercise;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundExercise extends CustomException {
    public NotFoundExercise() {
        super(ErrorCode.NOT_FOUND_EXERCISE);
    }

    public NotFoundExercise(String message) {
        super(ErrorCode.NOT_FOUND_EXERCISE, message);
    }

    public NotFoundExercise(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_EXERCISE, errorData);
    }
}
