package com.ogjg.daitgym.config.security.jwt.filter;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.config.security.jwt.authentication.JwtAuthenticationToken;
import com.ogjg.daitgym.config.security.jwt.exception.AccessTokenException;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.*;

@RequiredArgsConstructor
public class JwtAccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final List<String> permitUrlList;

    /**
     * AccessToken, RefreshToken에 사용하는 Provider의 기능이 완전히 같아서 1개의 Provider만 사용
     * Provider에서 발생한 예외가 AccessToken에서 발생한 예외인지, RefreshToken에서 발생한 예외인지 구분이 필요했습니다.
     * 그래서 기존 메시지를 집어넣어 되던졌습니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isPermitted(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication jwtAuthentication = authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);

            filterChain.doFilter(request, response);
        } catch (JwtException jwtException) {
            authenticationEntryPoint.commence(
                    request, response,
                    new AccessTokenException(ErrorCode.ACCESS_TOKEN_AUTHENTICATION_FAIL.getMessage() + SPACE + jwtException.getMessage())
            );
        }
    }

    private boolean isPermitted(String requestUri) {
        return permitUrlList.stream()
                .filter((pattern) -> Pattern.matches(pattern, requestUri))
                .findAny()
                .isPresent();
    }

    private Authentication authenticate(HttpServletRequest request) {
        String jwt = validAndGetAccessToken(request);
        Authentication authentication = authenticationManager.authenticate(new JwtAuthenticationToken(jwt));
        return authentication;
    }

    private String validAndGetAccessToken(HttpServletRequest request) {
        String jwt = request.getHeader(HEADER_AUTHORIZATION);

        TokenValidator.validateHasToken(jwt);
        TokenValidator.validatePrefix(jwt);

        return jwt.substring(TOKEN_PREFIX.length());
    }
}
