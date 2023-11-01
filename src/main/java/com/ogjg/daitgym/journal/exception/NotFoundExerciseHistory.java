package com.ogjg.daitgym.journal.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundExerciseHistory extends CustomException {
    public NotFoundExerciseHistory() {
        super(ErrorCode.NOT_FOUND_EXERCISE_HISTORY);
    }

    public NotFoundExerciseHistory(String message) {
        super(ErrorCode.NOT_FOUND_EXERCISE_HISTORY, message);
    }

    public NotFoundExerciseHistory(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_EXERCISE_HISTORY, errorData);
    }
}
