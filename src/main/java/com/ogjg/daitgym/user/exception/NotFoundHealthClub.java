package com.ogjg.daitgym.user.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundHealthClub extends CustomException {

    public NotFoundHealthClub() {
        super(ErrorCode.NOT_FOUND_HEALTH_CLUB);
    }

    public NotFoundHealthClub(String message) {
        super(ErrorCode.NOT_FOUND_HEALTH_CLUB, message);
    }

    public NotFoundHealthClub(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_HEALTH_CLUB, errorData);
    }
}
