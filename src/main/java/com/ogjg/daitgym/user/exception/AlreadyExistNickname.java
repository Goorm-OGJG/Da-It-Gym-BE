package com.ogjg.daitgym.user.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AlreadyExistNickname extends CustomException {
    public AlreadyExistNickname() {
        super(ErrorCode.ALREADY_EXIST_NICKNAME);
    }

    public AlreadyExistNickname(String message) {
        super(ErrorCode.ALREADY_EXIST_NICKNAME, message);
    }

    public AlreadyExistNickname(ErrorData errorData) {
        super(ErrorCode.ALREADY_EXIST_NICKNAME, errorData);
    }
}
