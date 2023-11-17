package com.ogjg.daitgym.common.exception.follow;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AlreadyFollowUser extends CustomException {
    public AlreadyFollowUser() {
        super(ErrorCode.ALREADY_FOLLOW_USER);
    }

    public AlreadyFollowUser(String message) {
        super(ErrorCode.ALREADY_FOLLOW_USER, message);
    }

    public AlreadyFollowUser(ErrorData errorData) {
        super(ErrorCode.ALREADY_FOLLOW_USER, errorData);
    }
}
