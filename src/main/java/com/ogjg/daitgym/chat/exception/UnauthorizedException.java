package com.ogjg.daitgym.chat.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final String errorType;
    private final String errorMessage;

    public UnauthorizedException(String errorType, String errorMessage) {
        super(errorMessage);
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    /**
     * of : 정적 팩토리 메서드
     * 정적 팩토리 메서드란, tatic Method를 통해 간접적으로 생성자를 호출하는 객체를 생성하는 디자인 패턴
     */
    public static UnauthorizedException of(String errorType, String errorMessage) {
        return new UnauthorizedException(errorType, errorMessage);
    }
}
