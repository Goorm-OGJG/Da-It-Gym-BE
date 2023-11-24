package com.ogjg.daitgym.common.exception.fcmtoken;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class NotFoundFcmToken extends CustomException {


    public NotFoundFcmToken() {
        super(ErrorCode.NOT_FOUND_FCM_TOKEN);
    }

    public NotFoundFcmToken(String message) {
        super(ErrorCode.NOT_FOUND_FCM_TOKEN, message);
    }

    public NotFoundFcmToken(ErrorData errorData) {
        super(ErrorCode.NOT_FOUND_FCM_TOKEN, errorData);
    }
}
