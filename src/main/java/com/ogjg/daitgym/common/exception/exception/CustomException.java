package com.ogjg.daitgym.common.exception.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final ErrorType errorCode;
    private ErrorData errorData;

    public CustomException(
            ErrorCode errorCode
    ) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(
            ErrorCode errorCode,
            String message
    ) {
        super(errorCode.changeMessage(message).getMessage());
        this.errorCode = errorCode.changeMessage(message);
    }

    public CustomException(
            ErrorCode errorCode,
            ErrorData errorData
    ) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorData = errorData;
    }
}
