package com.ogjg.daitgym.comment.routine.exception;


import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundRoutineComment extends CustomException {
    public NotFoundRoutineComment() {
        super(ErrorCode.NOT_FOUND_ROUTINE_COMMENT);
    }

    public NotFoundRoutineComment( String message) {
        super(ErrorCode.NOT_FOUND_ROUTINE_COMMENT, message);
    }

    public NotFoundRoutineComment( ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_ROUTINE_COMMENT, errorData);
    }
}
