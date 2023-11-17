package com.ogjg.daitgym.common.exception.routine;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AlreadyScrappedRoutine extends CustomException {
    public AlreadyScrappedRoutine() {
        super(ErrorCode.ALREADY_SCRAPPED_ROUTINE);
    }

    public AlreadyScrappedRoutine(String message) {
        super(ErrorCode.ALREADY_SCRAPPED_ROUTINE, message);
    }

    public AlreadyScrappedRoutine(ErrorData errorData) {
        super(ErrorCode.ALREADY_SCRAPPED_ROUTINE, errorData);
    }
}
