package com.ogjg.daitgym.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResult implements ErrorType {

    private final HttpStatus statusCode;
    private final String code;
    private final String message;

    public ErrorResult(HttpStatus statusCode, String code, String message) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
    }
}
