package com.ogjg.daitgym.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogjg.daitgym.config.security.jwt.handler.CustomAccessDeniedHandler;
import com.ogjg.daitgym.config.security.oauth.handler.Oauth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@RequiredArgsConstructor
public class HandlerConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new Oauth2AuthenticationSuccessHandler(httpSessionRequestCache(), objectMapper);
    }

    @Bean
    public RequestCache httpSessionRequestCache() {
        return new HttpSessionRequestCache();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(objectMapper);
    }
}


