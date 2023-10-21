package com.ogjg.daitgym.common.exception.response;

import com.ogjg.daitgym.common.exception.exception.ErrorType;
import com.ogjg.daitgym.common.exception.response.dto.Status;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private Status status;

    private T data;

    public ApiResponse(ErrorType errorCode) {
        this.status = new Status(errorCode);
    }

    public ApiResponse(ErrorType errorCode, T data) {
        this.status = new Status(errorCode);
        this.data = data;
    }
}
