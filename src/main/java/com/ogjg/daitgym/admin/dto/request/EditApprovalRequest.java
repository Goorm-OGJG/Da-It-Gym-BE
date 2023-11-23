package com.ogjg.daitgym.admin.dto.request;

import lombok.Getter;

@Getter
public class EditApprovalRequest {
    private String reason;
    private String approvalStatus;

    private String nickname;

    public EditApprovalRequest(String reason, String approvalStatus, String nickname) {
        this.reason = reason;
        this.approvalStatus = approvalStatus;
        this.nickname = nickname;
    }
}
