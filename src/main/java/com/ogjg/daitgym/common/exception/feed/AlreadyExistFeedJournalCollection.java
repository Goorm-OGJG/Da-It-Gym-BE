package com.ogjg.daitgym.common.exception.feed;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class AlreadyExistFeedJournalCollection extends CustomException {

    public AlreadyExistFeedJournalCollection() {
        super(ErrorCode.ALREADY_EXIST_FEED_COLLECTION);
    }

    public AlreadyExistFeedJournalCollection(String message) {
        super(ErrorCode.ALREADY_EXIST_FEED_COLLECTION, message);
    }

    public AlreadyExistFeedJournalCollection(ErrorData errorData) {
        super(ErrorCode.ALREADY_EXIST_FEED_COLLECTION, errorData);
    }
}
