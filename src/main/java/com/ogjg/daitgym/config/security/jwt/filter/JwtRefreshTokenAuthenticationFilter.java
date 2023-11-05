package com.ogjg.daitgym.config.security.jwt.filter;

import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.config.security.jwt.authentication.JwtAuthenticationToken;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.config.security.jwt.exception.RefreshTokenException;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.*;

@RequiredArgsConstructor
public class JwtRefreshTokenAuthenticationFilter extends OncePerRequestFilter {

    private final String TOKEN_REGENERATE_REQUEST_URL = "/api/users/token";

    private final AuthenticationManager authenticationManager;

    private final AuthenticationEntryPoint authenticationEntryPoint;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isAccessTokenExpired(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = generateAccessToken(authentication);
            addTokenInHeader(response, accessToken);

            filterChain.doFilter(request, response);
        } catch (JwtException jwtException) {
            authenticationEntryPoint.commence(
                    request, response,
                    new RefreshTokenException(ErrorCode.ACCESS_TOKEN_AUTHENTICATION_FAIL.getMessage() + SPACE + jwtException.getMessage())
            );
        }
    }

    private boolean isAccessTokenExpired(HttpServletRequest request) {
        return TOKEN_REGENERATE_REQUEST_URL.equals(request.getRequestURI());
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

    private static String generateAccessToken(Authentication authentication) {
        OAuth2JwtUserDetails userDetails = (OAuth2JwtUserDetails) authentication.getPrincipal();
        return TokenGenerator.generateAccessToken(JwtUserClaimsDto.from(userDetails));
    }

    private void addTokenInHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + accessToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHARSET_UTF_8);
    }
}
