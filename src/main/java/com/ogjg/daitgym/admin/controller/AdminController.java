package com.ogjg.daitgym.admin.controller;

import com.ogjg.daitgym.admin.dto.response.GetApprovalsResponse;
import com.ogjg.daitgym.admin.service.AdminService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins/approvals")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("")
    public ApiResponse<GetApprovalsResponse> getApprovals(
            @RequestParam("nickname") String nickname,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                adminService.getApprovals(nickname, pageable)
        );
    }
}
