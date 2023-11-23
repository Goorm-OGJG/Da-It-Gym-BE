package com.ogjg.daitgym.common.exception.user;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class ForbiddenKaKaoSocial extends CustomException {
    public ForbiddenKaKaoSocial() {
        super(ErrorCode.FORBIDDEN_KAKAO_SOCIAL);
    }

    public ForbiddenKaKaoSocial(String message) {
        super(ErrorCode.FORBIDDEN_KAKAO_SOCIAL, message);
    }

    public ForbiddenKaKaoSocial(ErrorData errorData) {
        super(ErrorCode.FORBIDDEN_KAKAO_SOCIAL, errorData);
    }
}
