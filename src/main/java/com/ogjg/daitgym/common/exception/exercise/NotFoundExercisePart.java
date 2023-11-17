package com.ogjg.daitgym.common.exception.exercise;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundExercisePart extends CustomException {
    public NotFoundExercisePart() {
        super(ErrorCode.NOT_FOUND_EXERCISE_PART);
    }

    public NotFoundExercisePart(String message) {
        super(ErrorCode.NOT_FOUND_EXERCISE_PART, message);
    }

    public NotFoundExercisePart(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_EXERCISE_PART, errorData);
    }
}
