package com.ogjg.daitgym.common.exception.routine;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundScrappedUserRoutine extends CustomException {
    public NotFoundScrappedUserRoutine() {
        super(ErrorCode.NOT_FOUND_SCRAPPED_USER_ROUTINE);
    }

    public NotFoundScrappedUserRoutine(String message) {
        super(ErrorCode.NOT_FOUND_SCRAPPED_USER_ROUTINE, message);
    }

    public NotFoundScrappedUserRoutine(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_SCRAPPED_USER_ROUTINE, errorData);
    }
}
