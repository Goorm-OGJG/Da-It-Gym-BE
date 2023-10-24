package com.ogjg.daitgym.common.exception.exception;

import org.springframework.http.HttpStatus;

public interface ErrorType {

    HttpStatus getStatusCode();

    String getCode();

    String getMessage();

}
