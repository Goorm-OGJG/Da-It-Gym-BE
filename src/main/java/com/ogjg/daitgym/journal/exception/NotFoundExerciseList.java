package com.ogjg.daitgym.journal.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundExerciseList extends CustomException {

    public NotFoundExerciseList() {
        super(ErrorCode.NOT_FOUND_EXERCISE_LIST);
    }

    public NotFoundExerciseList(String message) {
        super(ErrorCode.NOT_FOUND_EXERCISE_LIST, message);
    }

    public NotFoundExerciseList(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_EXERCISE_LIST, errorData);
    }

}
