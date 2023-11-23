package com.ogjg.daitgym.common.exception.user;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundUserAuthentication extends CustomException {

    public NotFoundUserAuthentication() {
        super(ErrorCode.NOT_FOUNT_USER_AUTHENTICATION);
    }

    public NotFoundUserAuthentication(String message) {
        super(ErrorCode.NOT_FOUNT_USER_AUTHENTICATION, message);
    }

    public NotFoundUserAuthentication(ErrorData errorData) {
        super(ErrorCode.NOT_FOUNT_USER_AUTHENTICATION, errorData);
    }
}
