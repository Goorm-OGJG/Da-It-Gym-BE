package com.ogjg.daitgym.common.exception.user;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundSplitTitle extends CustomException {
    public NotFoundSplitTitle() {
        super(ErrorCode.NOT_FOUND_SPLIT_TITLE);
    }

    public NotFoundSplitTitle(String message) {
        super(ErrorCode.NOT_FOUND_SPLIT_TITLE, message);
    }

    public NotFoundSplitTitle(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_SPLIT_TITLE, errorData);
    }
}
