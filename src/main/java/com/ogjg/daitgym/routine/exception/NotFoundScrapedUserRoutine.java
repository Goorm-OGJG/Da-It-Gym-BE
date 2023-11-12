package com.ogjg.daitgym.routine.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundScrapedUserRoutine extends CustomException {
    public NotFoundScrapedUserRoutine() {
        super(ErrorCode.NOT_FOUND_SCRAPED_USER_ROUTINE);
    }

    public NotFoundScrapedUserRoutine(String message) {
        super(ErrorCode.NOT_FOUND_SCRAPED_USER_ROUTINE, message);
    }

    public NotFoundScrapedUserRoutine(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_SCRAPED_USER_ROUTINE, errorData);
    }
}
