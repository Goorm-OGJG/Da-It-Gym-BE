package com.ogjg.daitgym.common.exception.user;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class UnauthorizedUser extends CustomException {
    public UnauthorizedUser() {
        super(ErrorCode.UNAUTHORIZED_USER_ACCESS);
    }

    public UnauthorizedUser(String message) {
        super(ErrorCode.UNAUTHORIZED_USER_ACCESS, message);
    }

    public UnauthorizedUser(ErrorData errorData) {
        super(ErrorCode.UNAUTHORIZED_USER_ACCESS, errorData);
    }
}