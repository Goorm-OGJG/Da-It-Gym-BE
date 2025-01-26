package com.ogjg.daitgym.config.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.config.security.jwt.authentication.JwtRefreshAuthenticationToken;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.config.security.jwt.exception.RefreshTokenException;
import com.ogjg.daitgym.config.security.jwt.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.ogjg.daitgym.config.security.jwt.constants.JwtConstants.ACCESS_TOKEN;
import static com.ogjg.daitgym.config.security.jwt.constants.JwtConstants.CHARSET_UTF_8;

@RequiredArgsConstructor
public class JwtRefreshTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final RequestMatcher REGENERATE_TOKEN_REQUEST;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!REGENERATE_TOKEN_REQUEST.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String refreshToken = JwtUtils.getRefreshTokenFrom(request);
            Authentication authentication = authenticationManager.authenticate(new JwtRefreshAuthenticationToken(refreshToken));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = generateAccessToken(authentication);

            ObjectMapper objectMapper = new ObjectMapper();
            ApiResponse<?> apiResponse = new ApiResponse<>(ErrorCode.SUCCESS);
            String successResponse = objectMapper.writeValueAsString(apiResponse);

            response.addHeader(ACCESS_TOKEN.HTTP_HEADER_AUTHORIZATION, accessToken);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(CHARSET_UTF_8);

            response.getWriter().write(successResponse);
            response.getWriter().flush();

        } catch (JwtException jwtException) {
            authenticationEntryPoint.commence(
                    request, response,
                    new RefreshTokenException(ErrorCode.ACCESS_TOKEN_AUTHENTICATION_FAIL.getMessage())
            );
        }
    }

    private static String generateAccessToken(Authentication authentication) {
        OAuth2JwtUserDetails userDetails = (OAuth2JwtUserDetails) authentication.getPrincipal();
        return JwtUtils.Generator.generateAccessToken(JwtUserClaimsDto.from(userDetails));
    }
}
