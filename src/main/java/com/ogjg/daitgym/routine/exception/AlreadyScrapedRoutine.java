package com.ogjg.daitgym.routine.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AlreadyScrapedRoutine extends CustomException {
    public AlreadyScrapedRoutine() {
        super(ErrorCode.ALREADY_SCRAPED_ROUTINE);
    }

    public AlreadyScrapedRoutine(String message) {
        super(ErrorCode.ALREADY_SCRAPED_ROUTINE, message);
    }

    public AlreadyScrapedRoutine(ErrorData errorData) {
        super(ErrorCode.ALREADY_SCRAPED_ROUTINE, errorData);
    }
}
