package com.ogjg.daitgym.comment.feedExerciseJournal.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundExerciseJournal extends CustomException {
    public NotFoundExerciseJournal() {
        super(ErrorCode.NOT_FOUND_EXERCISE_JOURNAL);
    }

    public NotFoundExerciseJournal( String message) {
        super(ErrorCode.NOT_FOUND_EXERCISE_JOURNAL, message);
    }

    public NotFoundExerciseJournal( ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_EXERCISE_JOURNAL, errorData);
    }
}
