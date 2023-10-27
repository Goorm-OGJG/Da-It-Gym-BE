package com.ogjg.daitgym.comment.feedExerciseJournal.exception;


import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundUser extends CustomException {
    public NotFoundUser() {
        super(ErrorCode.NOT_FOUND_USER);
    }

    public NotFoundUser(String message) {
        super(ErrorCode.NOT_FOUND_USER, message);
    }

    public NotFoundUser( ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_USER, errorData);
    }
}
