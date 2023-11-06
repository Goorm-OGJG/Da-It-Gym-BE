package com.ogjg.daitgym.user.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.user.dto.request.ApplyForApprovalRequest;
import com.ogjg.daitgym.user.dto.request.EditUserProfileRequest;
import com.ogjg.daitgym.user.dto.response.GetUserProfileGetResponse;
import com.ogjg.daitgym.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 프로필 불러오기
     */
    @GetMapping("/{nickname}")
    public ApiResponse<GetUserProfileGetResponse> getUserProfile(
            @PathVariable("nickname") String nickname,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        String loginEmail = userDetails.getEmail();
        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                userService.getUserProfile(loginEmail, nickname)
        );
    }

    /**
     * 프로필 편집
     */
    @PutMapping("/{nickname}")
    public ApiResponse<Void> editUserProfile(
            @PathVariable("nickname") String nickname,
            @RequestPart MultipartFile userProfileImg,
            @RequestPart EditUserProfileRequest request,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        String loginEmail = userDetails.getEmail();
        userService.editUserProfile(loginEmail, nickname, userProfileImg, request);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 트레이너 심사 신청
     */
    @PostMapping("/career/submit")
    public ApiResponse<Void> applyForApproval(
            @RequestPart ApplyForApprovalRequest request,
            @RequestPart List<MultipartFile> certificationImgs,
            @RequestPart List<MultipartFile> awardImgs,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        String loginEmail = userDetails.getEmail();

        userService.applyForApproval(loginEmail, request, awardImgs, certificationImgs);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }
}
