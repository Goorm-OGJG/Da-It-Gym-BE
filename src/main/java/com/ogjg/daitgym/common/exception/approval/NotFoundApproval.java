package com.ogjg.daitgym.common.exception.approval;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundApproval extends CustomException {
    public NotFoundApproval() {
        super(ErrorCode.NOT_FOUND_APPROVAL);
    }

    public NotFoundApproval(String message) {
        super(ErrorCode.NOT_FOUND_APPROVAL, message);
    }

    public NotFoundApproval(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_APPROVAL, errorData);
    }
}
