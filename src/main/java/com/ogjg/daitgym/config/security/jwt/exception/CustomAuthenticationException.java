package com.ogjg.daitgym.config.security.jwt.exception;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;
import com.ogjg.daitgym.common.exception.ErrorType;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;


@Getter
public abstract class CustomAuthenticationException extends AuthenticationException {

    private final ErrorType errorType;
    private ErrorData errorData;


    public CustomAuthenticationException(ErrorCode errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public CustomAuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode.changeMessage(message).getMessage());
        this.errorType = errorCode.changeMessage(message);
    }

    public CustomAuthenticationException(ErrorCode errorType, ErrorData errorData) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.errorData = errorData;
    }
}
