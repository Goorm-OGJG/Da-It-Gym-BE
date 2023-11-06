package com.ogjg.daitgym.config.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED_USER_ACCESS);
        String errorResponseData = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(errorResponseData);
    }
}
