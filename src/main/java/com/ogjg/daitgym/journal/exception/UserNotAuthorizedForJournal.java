package com.ogjg.daitgym.journal.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class UserNotAuthorizedForJournal extends CustomException {

    public UserNotAuthorizedForJournal() {
        super(ErrorCode.USER_NOT_AUTHORIZED_JOURNAL);
    }

    public UserNotAuthorizedForJournal(String message) {
        super(ErrorCode.USER_NOT_AUTHORIZED_JOURNAL, message);
    }

    public UserNotAuthorizedForJournal(ErrorData errorData) {
        super(ErrorCode.USER_NOT_AUTHORIZED_JOURNAL, errorData);
    }
}
