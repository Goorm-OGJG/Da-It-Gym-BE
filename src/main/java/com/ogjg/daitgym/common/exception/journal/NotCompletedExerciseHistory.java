package com.ogjg.daitgym.common.exception.journal;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotCompletedExerciseHistory extends CustomException {

    public NotCompletedExerciseHistory() {
        super(ErrorCode.NOT_COMPLETED_EXERCISE_HISTORY);
    }

    public NotCompletedExerciseHistory(String message) {
        super(ErrorCode.NOT_COMPLETED_EXERCISE_HISTORY, message);
    }

    public NotCompletedExerciseHistory(ErrorData errorData) {
        super(ErrorCode.NOT_COMPLETED_EXERCISE_HISTORY, errorData);
    }
}
