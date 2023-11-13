package com.ogjg.daitgym.feed.exception;

import com.ogjg.daitgym.common.exception.CustomException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorData;

public class RangeOverImages extends CustomException {

    public RangeOverImages() {
        super(ErrorCode.RANGE_OVER_IMAGES);
    }

    public RangeOverImages(String message) {
        super(ErrorCode.RANGE_OVER_IMAGES, message);
    }

    public RangeOverImages(ErrorData errorData) {
        super(ErrorCode.RANGE_OVER_IMAGES, errorData);
    }
}
