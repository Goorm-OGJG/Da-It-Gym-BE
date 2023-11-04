package com.ogjg.daitgym.config.security.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.OAuth2JwtUserDetails;
import com.ogjg.daitgym.config.security.oauth.dto.LoginResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final String HOME_URL = "/";

    private final RequestCache requestCache;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2JwtUserDetails OAuth2UserDetails = (OAuth2JwtUserDetails) authentication.getPrincipal();
        String cachedUrl = getCachedUrlOrDefault(request, response);

        objectMapper.writeValue(
                response.getWriter(),
                new ApiResponse<>(
                        ErrorCode.SUCCESS,
                        LoginResponseDto.of(OAuth2UserDetails, cachedUrl)
                )
        );
    }

    private String getCachedUrlOrDefault(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            return savedRequest.getRedirectUrl();
        } else {
            // 세션 만료 대비
            return HOME_URL;
        }
    }
}
