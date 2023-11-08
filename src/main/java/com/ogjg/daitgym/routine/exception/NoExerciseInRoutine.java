package com.ogjg.daitgym.routine.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NoExerciseInRoutine extends CustomException {
    public NoExerciseInRoutine() {
        super(ErrorCode.NO_EXERCISE_IN_ROUTINE);
    }

    public NoExerciseInRoutine(String message) {
        super(ErrorCode.NO_EXERCISE_IN_ROUTINE, message);
    }

    public NoExerciseInRoutine(ErrorData errorData) {
        super(ErrorCode.NO_EXERCISE_IN_ROUTINE, errorData);
    }
}
