package com.ogjg.daitgym.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class RequestMatcherConfig {

    @Bean(name = "PERMIT_ALL_REQUEST")
    public RequestMatcher permitAllRequest(
            RequestMatcher REGENERATE_TOKEN_REQUEST,
            RequestMatcher HEALTH_CHECK_REQUEST,
            RequestMatcher KAKAO_LOGIN_REQUEST,
            RequestMatcher CHAT_REQUEST
    ) {
        return new OrRequestMatcher(
                REGENERATE_TOKEN_REQUEST,
                HEALTH_CHECK_REQUEST,
                KAKAO_LOGIN_REQUEST,
                CHAT_REQUEST
        );
    }

    @Bean(name = "REGENERATE_TOKEN_REQUEST")
    public RequestMatcher regenerateTokenRequest() {
        return antMatcher(HttpMethod.POST, "/api/users/token");
    }

    @Bean(name = "HEALTH_CHECK_REQUEST")
    public RequestMatcher healthCheckRequest() {
        return antMatcher(HttpMethod.GET, "/health");
    }

    @Bean(name = "KAKAO_LOGIN_REQUEST")
    public RequestMatcher kakaoLoginRequest() {
        return new OrRequestMatcher(
                antMatcher(HttpMethod.GET, "/login/oauth2/callback/kakao"),
                antMatcher(HttpMethod.GET, "/login/oauth2/code/")
        );
    }

    @Bean(name = "CHAT_REQUEST")
    public RequestMatcher chatRequest() {
        return new OrRequestMatcher(
                antMatcher("/ws/**"),
                antMatcher("/chat/**")
        );
    }
}