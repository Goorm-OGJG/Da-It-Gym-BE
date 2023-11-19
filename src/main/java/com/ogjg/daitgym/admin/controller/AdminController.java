package com.ogjg.daitgym.admin.controller;

import com.ogjg.daitgym.admin.dto.request.EditApprovalRequest;
import com.ogjg.daitgym.admin.dto.response.GetApprovalDetailResponse;
import com.ogjg.daitgym.admin.dto.response.GetApprovalsResponse;
import com.ogjg.daitgym.admin.service.AdminService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins/approvals")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("")
    public ApiResponse<GetApprovalsResponse> getApprovals(
            @RequestParam(value = "nickname", required = false, defaultValue = "") String nickname,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                adminService.getApprovals(nickname, pageable)
        );
    }

    @GetMapping("/{approvalId}")
    public ApiResponse<GetApprovalDetailResponse> getApproval(
            @PathVariable("approvalId") Long approvalId) {

        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                adminService.getApproval(approvalId)
        );
    }

    @PatchMapping("/{approvalId}")
    public ApiResponse<Void> handleApproval(
            @PathVariable("approvalId") Long approvalId,
            @RequestBody EditApprovalRequest request,
            @AuthenticationPrincipal OAuth2JwtUserDetails jwtUserDetails
            ) {
        adminService.updateApproval(approvalId, request, jwtUserDetails.getEmail());
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

}
