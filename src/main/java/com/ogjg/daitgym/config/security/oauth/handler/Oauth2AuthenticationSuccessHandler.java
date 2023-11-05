package com.ogjg.daitgym.config.security.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.config.security.oauth.dto.LoginResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.TokenGenerator;

@Slf4j
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String TOKEN_TYPE = "Bearer ";

    private static final String AUTH_HEADER = "Authorization";

    private final String REFRESH_TOKEN = "refreshToken";

    private static final String HOME_URL = "/";

    private final int EXPIRATION = 60 * 60 * 24 * 30;

    private final RequestCache requestCache;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2JwtUserDetails OAuth2UserDetails = (OAuth2JwtUserDetails) authentication.getPrincipal();
        JwtUserClaimsDto jwtUserClaimsDto = JwtUserClaimsDto.from(OAuth2UserDetails);

        addTokensInHeader(response, jwtUserClaimsDto);

        String cachedUrl = getCachedUrlOrDefault(request, response);

        objectMapper.writeValue(
                response.getWriter(),
                new ApiResponse<>(
                        ErrorCode.SUCCESS,
                        LoginResponseDto.of(OAuth2UserDetails, cachedUrl)
                )
        );
    }

    private void addTokensInHeader(HttpServletResponse response, JwtUserClaimsDto jwtUserClaimsDto) {
        String accessToken = TokenGenerator.generateAccessToken(jwtUserClaimsDto);
        String refreshToken = TokenGenerator.generateRefreshToken(jwtUserClaimsDto);

        response.addHeader(AUTH_HEADER, TOKEN_TYPE + accessToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Set-Cookie", createRefreshTokenCookie(refreshToken).toString());
        response.setCharacterEncoding("UTF-8");
    }

    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .maxAge(EXPIRATION)
                .path(HOME_URL)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
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
