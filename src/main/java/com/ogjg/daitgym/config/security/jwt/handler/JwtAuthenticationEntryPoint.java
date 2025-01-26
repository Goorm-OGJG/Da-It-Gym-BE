package com.ogjg.daitgym.config.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogjg.daitgym.common.exception.ErrorCode;
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
        ErrorResponse errorResponse = null;

        if (authException instanceof CustomAuthenticationException customAuthEx) {
            response.setStatus(customAuthEx.getErrorType().getStatusCode().value());
            errorResponse = new ErrorResponse(customAuthEx.getErrorType(), customAuthEx.getErrorData());

        } else {
            response.setStatus(ErrorCode.AUTHENTICATION_FAIL.getStatusCode().value());
            errorResponse = new ErrorResponse(ErrorCode.AUTHENTICATION_FAIL);
        }

        writeResponse(response, errorResponse);
    }

    private static void writeResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponseData = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(errorResponseData);
    }
}
