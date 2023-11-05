package com.ogjg.daitgym.config.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogjg.daitgym.common.response.ErrorResponse;
import com.ogjg.daitgym.config.security.jwt.exception.CustomAuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        CustomAuthenticationException tokenException = (CustomAuthenticationException) authException;

        response.setStatus(tokenException.getErrorType().getStatusCode().value());

        // todo : 에러데이터 확인
        ErrorResponse errorResponse = new ErrorResponse(tokenException.getErrorType());
        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponseData = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(errorResponseData);
    }
}
