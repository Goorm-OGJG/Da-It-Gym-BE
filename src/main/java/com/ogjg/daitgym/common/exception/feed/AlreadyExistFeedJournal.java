package com.ogjg.daitgym.common.exception.feed;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AlreadyExistFeedJournal extends CustomException {

    public AlreadyExistFeedJournal() {
        super(ErrorCode.ALREADY_SHARED_JOURNAL);
    }

    public AlreadyExistFeedJournal(String message) {
        super(ErrorCode.ALREADY_SHARED_JOURNAL, message);
    }

    public AlreadyExistFeedJournal(ErrorData errorData) {
        super(ErrorCode.ALREADY_SHARED_JOURNAL, errorData);
    }
}
