package com.ogjg.daitgym.config.security.jwt.exception;


import com.ogjg.daitgym.common.exception.ErrorCode;

public class RefreshTokenException extends CustomAuthenticationException {

    public RefreshTokenException() {
        super(ErrorCode.REFRESH_TOKEN_AUTHENTICATION_FAIL);
    }

    public RefreshTokenException(String message) {
        super(ErrorCode.REFRESH_TOKEN_AUTHENTICATION_FAIL, message);
    }

}
