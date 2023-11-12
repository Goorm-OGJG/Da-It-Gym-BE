package com.ogjg.daitgym.user.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.user.dto.request.ApplyForApprovalRequest;
import com.ogjg.daitgym.user.dto.request.EditUserProfileRequest;
import com.ogjg.daitgym.user.dto.request.RegisterInbodyRequest;
import com.ogjg.daitgym.user.dto.response.GetInbodiesResponse;
import com.ogjg.daitgym.user.dto.response.GetUserProfileGetResponse;
import com.ogjg.daitgym.user.dto.request.EditNicknameRequest;
import com.ogjg.daitgym.user.dto.response.EditInitialNicknameResponse;
import com.ogjg.daitgym.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 로그아웃 - 메시지 지정 필요
     */
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response) {
        response.setHeader("Set-Cookie", userService.getExpiredResponseCookie().toString());
        return new ApiResponse<>(ErrorCode.SUCCESS.changeMessage("로그아웃 성공"));
    }

    /**
     * 닉네임 초기 변경
     */
    @PatchMapping("/nickname")
    public ApiResponse<EditInitialNicknameResponse> editInitialNickname(
            @RequestBody EditNicknameRequest request,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        String loginEmail = userDetails.getEmail();

        return new ApiResponse<>(
                ErrorCode.SUCCESS.changeMessage("닉네임 변경 완료"),
                userService.editInitialNickname(loginEmail, request)
        );
    }

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

    /**
     * 인바디 등록
     */
    @PostMapping("/inbodies")
    public ApiResponse<Void> registerInbody(
            @RequestBody RegisterInbodyRequest request,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        String loginEmail = userDetails.getEmail();

        userService.registerInbody(loginEmail, request);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 인바디 조회
     */
    @GetMapping("/{nickname}/inbodies")
    public ApiResponse<GetInbodiesResponse> getInbodies(
            @PathVariable("nickname") String nickname,
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) {
        String loginEmail = userDetails.getEmail();

        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                userService.getInbodies(nickname)
        );
    }
}
