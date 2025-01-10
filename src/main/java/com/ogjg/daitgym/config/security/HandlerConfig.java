package com.ogjg.daitgym.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogjg.daitgym.config.security.jwt.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
public class HandlerConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(objectMapper);
    }
}


