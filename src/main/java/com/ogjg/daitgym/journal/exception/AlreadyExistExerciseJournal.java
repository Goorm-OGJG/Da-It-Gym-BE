package com.ogjg.daitgym.journal.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AlreadyExistExerciseJournal extends CustomException {

    public AlreadyExistExerciseJournal() {
        super(ErrorCode.ALREADY_EXIST_EXERCISE_JOURNAL);
    }

    public AlreadyExistExerciseJournal(String message) {
        super(ErrorCode.ALREADY_EXIST_EXERCISE_JOURNAL, message);
    }

    public AlreadyExistExerciseJournal(ErrorData errorData) {
        super(ErrorCode.ALREADY_EXIST_EXERCISE_JOURNAL, errorData);
    }
}
