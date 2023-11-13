package com.ogjg.daitgym.user.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class EmptyTrainerApplyException extends CustomException {

    public EmptyTrainerApplyException() {
        super(ErrorCode.EMPTY_TRAINER_APPLY_APPROVAL);
    }

    public EmptyTrainerApplyException(String message) {
        super(ErrorCode.EMPTY_TRAINER_APPLY_APPROVAL, message);
    }

    public EmptyTrainerApplyException(ErrorData errorData) {
        super(ErrorCode.EMPTY_TRAINER_APPLY_APPROVAL, errorData);
    }
}
