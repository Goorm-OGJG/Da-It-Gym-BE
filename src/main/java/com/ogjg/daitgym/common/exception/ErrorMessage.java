package com.ogjg.daitgym.common.exception;

public record ErrorMessage(String errorMessage) implements ErrorData {
    public static ErrorMessage from(String message) {
        return new ErrorMessage(message);
    }

    public static ErrorMessage from(Exception exception) {
        return new ErrorMessage(exception.getMessage());
    }
}
