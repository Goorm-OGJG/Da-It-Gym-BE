package com.ogjg.daitgym.common.exception.follow;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundFollow extends CustomException {
    public NotFoundFollow() {
        super(ErrorCode.NOT_FOUND_FOLLOW);
    }

    public NotFoundFollow(String message) {
        super(ErrorCode.NOT_FOUND_FOLLOW, message);
    }

    public NotFoundFollow(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_FOLLOW, errorData);
    }
}
