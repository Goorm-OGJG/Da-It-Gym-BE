package com.ogjg.daitgym.domain;

import com.ogjg.daitgym.common.exception.approval.NotFoundApprovalStatus;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ApproveStatus {

    // 관리자- 승인요청 목록 불러오기에서 1차적으로 enum 배치 순서대로 정렬됩니다.
    WAITING("승인 대기"), SUSPENSION("보류"), REJECTION("승인 거부"), APPROVAL("트레이너 승인");

    private final String title;

    public static ApproveStatus from(String title) {
        return Arrays.stream(values())
                .filter((value) -> title.equals(value.title))
                .findAny()
                .orElseThrow(NotFoundApprovalStatus::new);
    }

    ApproveStatus(String title) {
        this.title = title;
    }
}
