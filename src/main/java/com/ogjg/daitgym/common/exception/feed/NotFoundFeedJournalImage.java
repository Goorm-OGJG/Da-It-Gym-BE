package com.ogjg.daitgym.common.exception.feed;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundFeedJournalImage extends CustomException {
    public NotFoundFeedJournalImage() {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL_IMAGE);
    }

    public NotFoundFeedJournalImage(String message) {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL_IMAGE, message);
    }

    public NotFoundFeedJournalImage(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL_IMAGE, errorData);
    }
}
