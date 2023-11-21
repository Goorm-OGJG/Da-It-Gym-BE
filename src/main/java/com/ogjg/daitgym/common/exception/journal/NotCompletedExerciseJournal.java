package com.ogjg.daitgym.common.exception.journal;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotCompletedExerciseJournal extends CustomException {

    public NotCompletedExerciseJournal() {
        super(ErrorCode.NOT_COMPLETED_EXERCISE_JOURNAL);
    }

    public NotCompletedExerciseJournal(String message) {
        super(ErrorCode.NOT_COMPLETED_EXERCISE_JOURNAL, message);
    }

    public NotCompletedExerciseJournal(ErrorData errorData) {
        super(ErrorCode.NOT_COMPLETED_EXERCISE_JOURNAL, errorData);
    }
}
