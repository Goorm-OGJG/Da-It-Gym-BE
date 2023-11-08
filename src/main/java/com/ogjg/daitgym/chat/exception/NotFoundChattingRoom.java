package com.ogjg.daitgym.chat.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundChattingRoom extends CustomException {
    public NotFoundChattingRoom() {
        super(ErrorCode.NOT_FOUND_CHATTING_ROOM);
    }

    public NotFoundChattingRoom( String message) {
        super(ErrorCode.NOT_FOUND_CHATTING_ROOM, message);
    }

    public NotFoundChattingRoom( ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_CHATTING_ROOM, errorData);
    }
}
