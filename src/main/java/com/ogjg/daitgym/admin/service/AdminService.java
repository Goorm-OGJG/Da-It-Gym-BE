package com.ogjg.daitgym.admin.service;

import com.ogjg.daitgym.admin.dto.request.EditApprovalRequest;
import com.ogjg.daitgym.admin.dto.response.GetApprovalDetailResponse;
import com.ogjg.daitgym.admin.dto.response.GetApprovalsResponse;
import com.ogjg.daitgym.approval.repository.ApprovalRepository;
import com.ogjg.daitgym.approval.repository.AwardRepository;
import com.ogjg.daitgym.approval.repository.CertificationRepository;
import com.ogjg.daitgym.common.exception.approval.NotFoundApproval;
import com.ogjg.daitgym.domain.Approval;
import com.ogjg.daitgym.domain.ApproveStatus;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.repository.UserRepository;
import com.ogjg.daitgym.user.service.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ApprovalRepository approvalRepository;
    private final AwardRepository awardRepository;
    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final UserHelper userHelper;

    @Transactional(readOnly = true)
    public GetApprovalsResponse getApprovals(String nickname, Pageable pageable) {
        List<User> users = userRepository.findByNicknameStartingWith(nickname);
        List<Approval> approvals = users.stream()
                .map(User::getEmail)
                .flatMap((email) -> getApprovals(email).stream())
                .distinct().toList();

        List<Approval> pagedApprovals = approvals.stream()
                .sorted(comparing(Approval::getApproveStatus).thenComparing(Approval::getId))
                .skip(getStartPage(pageable))
                .limit(pageable.getPageSize())
                .collect(toList());

        return GetApprovalsResponse.of(
                calculateTotalPages(approvals.size(), pageable),
                pagedApprovals
        );
    }

    private long calculateTotalPages(long totalCount, Pageable pageable) {
        long pageCount = totalCount / pageable.getPageSize();
        if (totalCount % pageable.getPageSize() != 0) pageCount++;
        return pageCount;
    }

    private List<Approval> getApprovals(String findEmail) {
        List<Approval> approvals = new ArrayList<>();

        approvals.addAll(
                certificationRepository.findByUserEmail(findEmail).stream()
                        .map((award -> award.getApproval()))
                        .collect(toList())
        );
        approvals.addAll(
                awardRepository.findByUserEmail(findEmail).stream()
                        .map((certification -> certification.getApproval()))
                        .collect(toList())
        );

        return approvals;
    }

    private int getStartPage(Pageable pageable) {
        return (pageable.getPageNumber() - 1) * pageable.getPageSize();
    }

    @Transactional(readOnly = true)
    public GetApprovalDetailResponse getApproval(Long approvalId) {
        Approval approval = findById(approvalId);
        return GetApprovalDetailResponse.from(approval);
    }

    @Transactional
    public void updateApproval(Long approvalId, EditApprovalRequest request, String loginEmail) {
        Approval approval = findById(approvalId);
        ApproveStatus approveStatus = ApproveStatus.from(request.getApprovalStatus());
        approval.edit(approveStatus, request.getReason(), loginEmail);

        if (approveStatus == ApproveStatus.APPROVAL) {
            User findUser = userHelper.findUserByNickname(request.getNickname());
            findUser.promoteToTrainer();
        }
    }

    private Approval findById(Long approvalId) {
        return approvalRepository.findById(approvalId)
                .orElseThrow(NotFoundApproval::new);
    }
}
