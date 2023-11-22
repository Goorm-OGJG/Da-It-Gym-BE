package com.ogjg.daitgym.common.exception.user;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AlreadyProceedingApproval extends CustomException {

    public AlreadyProceedingApproval() {
        super(ErrorCode.ALREADY_PROCEEDING_APPROVAL);
    }

    public AlreadyProceedingApproval(String message) {
        super(ErrorCode.ALREADY_PROCEEDING_APPROVAL, message);
    }

    public AlreadyProceedingApproval(ErrorData errorData) {
        super(ErrorCode.ALREADY_PROCEEDING_APPROVAL, errorData);
    }
}
