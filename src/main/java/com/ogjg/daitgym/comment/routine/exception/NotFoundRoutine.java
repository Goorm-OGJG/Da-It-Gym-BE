package com.ogjg.daitgym.comment.routine.exception;


import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundRoutine extends CustomException {
    public NotFoundRoutine( ) {
        super(ErrorCode.NOT_FOUND_ROUTINE);
    }

    public NotFoundRoutine( String message) {
        super(ErrorCode.NOT_FOUND_ROUTINE, message);
    }

    public NotFoundRoutine( ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_ROUTINE, errorData);
    }
}
