package com.ogjg.daitgym.common.exception.response;

import com.ogjg.daitgym.common.exception.exception.ErrorData;
import com.ogjg.daitgym.common.exception.exception.ErrorType;

public class ErrorResponse extends ApiResponse<ErrorData> {

    public ErrorResponse(ErrorType errorCode) {
        super(errorCode);
    }

    public ErrorResponse(ErrorType errorCode, ErrorData errorData) {
        super(errorCode, errorData);
    }

}
