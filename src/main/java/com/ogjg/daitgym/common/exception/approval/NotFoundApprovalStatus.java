package com.ogjg.daitgym.common.exception.approval;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundApprovalStatus extends CustomException {
    public NotFoundApprovalStatus() {
        super(ErrorCode.NOT_FOUND_APPROVAL_STATUS);
    }

    public NotFoundApprovalStatus(String message) {
        super(ErrorCode.NOT_FOUND_APPROVAL, message);
    }

    public NotFoundApprovalStatus(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_APPROVAL, errorData);
    }
}
