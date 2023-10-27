package com.ogjg.daitgym.comment.feedExerciseJournal.exception;


import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class WrongApproach extends CustomException {
    public WrongApproach() {
        super(ErrorCode.WRONG_APPROACH);
    }

    public WrongApproach( String message) {
        super(ErrorCode.WRONG_APPROACH, message);
    }

    public WrongApproach( ErrorData errorData) {
        super(ErrorCode.WRONG_APPROACH, errorData);
    }
}
