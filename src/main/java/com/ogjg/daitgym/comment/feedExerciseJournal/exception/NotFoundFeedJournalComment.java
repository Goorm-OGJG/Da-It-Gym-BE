package com.ogjg.daitgym.comment.feedExerciseJournal.exception;


import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundFeedJournalComment extends CustomException {
    public NotFoundFeedJournalComment() {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL_COMMENT);
    }

    public NotFoundFeedJournalComment( String message) {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL_COMMENT, message);
    }

    public NotFoundFeedJournalComment( ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_FEED_JOURNAL_COMMENT, errorData);
    }
}
