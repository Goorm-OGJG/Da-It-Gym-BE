package com.ogjg.daitgym.user.controller;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.user.dto.response.KakaoTokenResponse;
import com.ogjg.daitgym.user.dto.response.LoginResponse;
import com.ogjg.daitgym.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @GetMapping("/login/oauth2/callback/kakao")
    public ApiResponse<LoginResponse> kakaoLogin(
            @RequestParam("code") String code,
            HttpServletResponse httpServletResponse
    ) {
        KakaoTokenResponse kakaoTokenResponse = authService.getKakaoAccessToken(code);

        return new ApiResponse<>(
                ErrorCode.SUCCESS,
                authService.kakaoLogin(kakaoTokenResponse, httpServletResponse)
        );
    }
}
