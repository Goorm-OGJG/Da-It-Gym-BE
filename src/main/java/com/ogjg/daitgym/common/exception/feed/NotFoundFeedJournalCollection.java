package com.ogjg.daitgym.common.exception.feed;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundFeedJournalCollection extends CustomException {
    public NotFoundFeedJournalCollection() {
        super(ErrorCode.NOT_FOUND_EXERCISE_COLLECTION);
    }

    public NotFoundFeedJournalCollection(String message) {
        super(ErrorCode.NOT_FOUND_EXERCISE_COLLECTION, message);
    }

    public NotFoundFeedJournalCollection(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_EXERCISE_COLLECTION, errorData);
    }
}
