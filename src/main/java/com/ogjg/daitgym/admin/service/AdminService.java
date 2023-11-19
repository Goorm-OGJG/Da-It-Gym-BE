package com.ogjg.daitgym.admin.service;

import com.ogjg.daitgym.admin.dto.request.EditApprovalRequest;
import com.ogjg.daitgym.admin.dto.response.GetApprovalDetailResponse;
import com.ogjg.daitgym.admin.dto.response.GetApprovalsResponse;
import com.ogjg.daitgym.approval.repository.ApprovalRepository;
import com.ogjg.daitgym.common.exception.approval.NotFoundApproval;
import com.ogjg.daitgym.domain.Approval;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ApprovalRepository approvalRepository;

    @Transactional(readOnly = true)
    public GetApprovalsResponse getApprovals(String nickname, Pageable pageable) {
        return GetApprovalsResponse.of(
                calculateTotalPages(nickname, pageable),
                approvalRepository.findByUserNickname(nickname, pageable.getPageSize(), pageable.getPageNumber())
        );
    }

    private long calculateTotalPages(String nickname, Pageable pageable) {
        long totalCount = approvalRepository.countTotalPageByUserNickname(nickname);
        long pageCount = totalCount / pageable.getPageSize();
        if (totalCount % pageable.getPageSize() != 0) pageCount++;
        return pageCount;
    }

    @Transactional(readOnly = true)
    public GetApprovalDetailResponse getApproval(Long approvalId) {
        Approval approval = findById(approvalId);

        return GetApprovalDetailResponse.from(approval);
    }

    @Transactional
    public void updateApproval(Long approvalId, EditApprovalRequest request, String loginEmail) {
        Approval approval = findById(approvalId);
        approval.edit(request.getApprovalStatus(), request.getReason(), loginEmail);
    }

    private Approval findById(Long approvalId) {
        return approvalRepository.findById(approvalId)
                .orElseThrow(NotFoundApproval::new);
    }
}
