package com.ogjg.daitgym.journal.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundJournal extends CustomException {

    public NotFoundJournal() {
        super(ErrorCode.NOT_FOUND_JOURNAL);
    }

    public NotFoundJournal(String message) {
        super(ErrorCode.NOT_FOUND_JOURNAL, message);
    }

    public NotFoundJournal(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_JOURNAL, errorData);
    }
}
