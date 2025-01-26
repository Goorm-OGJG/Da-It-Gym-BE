package com.ogjg.daitgym.config.security.jwt.exception;


import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AccessTokenException extends CustomAuthenticationException {

    public AccessTokenException() {
        super(ErrorCode.ACCESS_TOKEN_AUTHENTICATION_FAIL);
    }

    public AccessTokenException(String message) {
        super(ErrorCode.ACCESS_TOKEN_AUTHENTICATION_FAIL, message);
    }

    public AccessTokenException(ErrorCode errorType, ErrorData errorData) {
        super(errorType, errorData);
    }
}
