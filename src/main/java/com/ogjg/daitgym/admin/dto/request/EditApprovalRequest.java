package com.ogjg.daitgym.admin.dto.request;

import lombok.Getter;

@Getter
public class EditApprovalRequest {
    private String reason;
    private String approvalStatus;

    public EditApprovalRequest(String reason, String approvalStatus) {
        this.reason = reason;
        this.approvalStatus = approvalStatus;
    }
}
