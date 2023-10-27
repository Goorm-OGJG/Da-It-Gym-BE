package com.ogjg.daitgym.comment.feedExerciseJournal.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundFeedJournal extends CustomException {
    public NotFoundFeedJournal() {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL);
    }

    public NotFoundFeedJournal(String message) {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL, message);
    }

    public NotFoundFeedJournal(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL, errorData);
    }
}
