package com.ogjg.daitgym.config.security.jwt.filter;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.exception.ErrorMessage;
import com.ogjg.daitgym.config.security.jwt.authentication.JwtAccessAuthenticationToken;
import com.ogjg.daitgym.config.security.jwt.exception.AccessTokenException;
import com.ogjg.daitgym.config.security.jwt.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final RequestMatcher PERMIT_ALL_REQUEST;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (PERMIT_ALL_REQUEST.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = JwtUtils.getAccessTokenFrom(request);
            Authentication jwtAuthentication = authenticationManager.authenticate(new JwtAccessAuthenticationToken(accessToken));
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);

            filterChain.doFilter(request, response);
        } catch (JwtException jwtException) {
            authenticationEntryPoint.commence(
                    request, response,
                    new AccessTokenException(ErrorCode.ACCESS_TOKEN_AUTHENTICATION_FAIL, ErrorMessage.from(jwtException))
            );
        }
    }
}
