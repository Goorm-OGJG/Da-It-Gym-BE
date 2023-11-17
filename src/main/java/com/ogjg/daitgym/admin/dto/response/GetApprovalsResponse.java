package com.ogjg.daitgym.admin.dto.response;

import com.ogjg.daitgym.domain.Approval;
import com.ogjg.daitgym.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class GetApprovalsResponse {

    private final List<GetApprovalResponse> approvals;

    public GetApprovalsResponse(List<GetApprovalResponse> approvals) {
        this.approvals = approvals;
    }

    public static GetApprovalsResponse from(List<Approval> approvalPages) {
        List<GetApprovalResponse> approvals = approvalPages.stream()
                .map(GetApprovalResponse::from)
                .toList();
        return new GetApprovalsResponse(approvals);
    }

    @Getter
    private static class GetApprovalResponse {
        private Long approvalId;
        private String nickname;
        private String email;
        private String role;
        private String approvalStatus;
        private boolean withdraw;
        private LocalDate joinAt;

        @Builder
        public GetApprovalResponse(Long approvalId, String nickname, String email, String role, String approvalStatus, boolean withdraw, LocalDate joinAt) {
            this.approvalId = approvalId;
            this.nickname = nickname;
            this.email = email;
            this.role = role;
            this.approvalStatus = approvalStatus;
            this.withdraw = withdraw;
            this.joinAt = joinAt;
        }

        public static GetApprovalResponse from(Approval approval) {
            User user = findUser(approval);

            return GetApprovalResponse.builder()
                    .approvalId(approval.getId())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .role(user.getRole().getTitle())
                    .approvalStatus(approval.getApproveStatus().getTitle())
                    .withdraw(user.isDeleted())
                    .joinAt(user.getCreatedAt().toLocalDate())
                    .build();
        }

        private static User findUser(Approval approval) {
            if (approval.getAwards() == null) {
                return approval.getCertifications().get(0).getUser();
            }

            return approval.getAwards().get(0).getUser();
        }
    }
}
