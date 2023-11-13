package com.ogjg.daitgym.chat.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;

public class NotFoundChattingRoom extends CustomException {
    public NotFoundChattingRoom(String message) {
        super(ErrorCode.NOT_FOUND_CHATTING_ROOM, message);
    }
}
