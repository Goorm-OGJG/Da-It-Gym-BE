package com.ogjg.daitgym.domain;

import lombok.Getter;

@Getter
public enum ApproveStatus {

    // 관리자- 승인요청 목록 불러오기에서 1차적으로 enum 배치 순서대로 정렬됩니다.
    WAITING("승인 대기"), SUSPENSION("보류"), REJECTION("승인 거부"), APPROVAL("트레이너 승인");

    private final String title;

    ApproveStatus(String title) {
        this.title = title;
    }
}
