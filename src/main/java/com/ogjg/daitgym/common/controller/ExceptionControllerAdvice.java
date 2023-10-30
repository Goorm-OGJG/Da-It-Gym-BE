package com.ogjg.daitgym.common.controller;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ApiResponse<?> customExceptionHandler(
            HttpServletResponse response, CustomException e
    ) {
        response.setStatus(e.getErrorCode().getStatusCode().value());
        log.error(e.getMessage());
        return new ErrorResponse(e.getErrorCode(), e.getErrorData());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse<?> validationHandler(
            HttpServletResponse response, MethodArgumentNotValidException e
    ) {
        response.setStatus(ErrorCode.INVALID_FORMAT.getStatusCode().value());
        String errorMessage = validationErrorMessage(e.getBindingResult().getFieldError());
        log.error(errorMessage);
        return new ErrorResponse(ErrorCode.INVALID_FORMAT.changeMessage(errorMessage));
    }

    private String validationErrorMessage(FieldError fieldError) {
        if (fieldError != null) {
            return fieldError.getDefaultMessage();
        }
        return ErrorCode.INVALID_FORMAT.getMessage();
    }
}