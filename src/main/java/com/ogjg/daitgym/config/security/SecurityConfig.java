package com.ogjg.daitgym.config.security;

import com.ogjg.daitgym.config.security.jwt.authentication.JwtAuthenticationProvider;
import com.ogjg.daitgym.config.security.jwt.filter.JwtAccessTokenAuthenticationFilter;
import com.ogjg.daitgym.config.security.jwt.filter.JwtRefreshTokenAuthenticationFilter;
import com.ogjg.daitgym.config.security.jwt.handler.JwtAuthenticationEntryPoint;
import com.ogjg.daitgym.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final AccessDeniedHandler accessDeniedHandler;

    private final RequestMatcher PERMIT_ALL_REQUEST;

    private final RequestMatcher REGENERATE_TOKEN_REQUEST;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers((headers ->
                                headers
                                        .frameOptions(Customizer.withDefaults())
                                        .frameOptions((frameOptionsConfig) -> frameOptionsConfig.sameOrigin())
                        )
                )
                .addFilterBefore(jwtAccessTokenAuthenticationFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
                .addFilterAfter(jwtRefreshTokenAuthenticationFilter(), JwtAccessTokenAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/admins/**")).hasRole(Role.ADMIN.name())
                                .requestMatchers(new AntPathRequestMatcher("/api/trainers/**")).hasAnyRole(Role.TRAINER.name(), Role.ADMIN.name())
                                .requestMatchers(PERMIT_ALL_REQUEST).permitAll()
                                .anyRequest().authenticated()
                ).exceptionHandling((exceptionHandle) -> exceptionHandle
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAccessTokenAuthenticationFilter jwtAccessTokenAuthenticationFilter() throws Exception {
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider());
        return new JwtAccessTokenAuthenticationFilter(authenticationManager(), jwtAuthenticationEntryPoint(), PERMIT_ALL_REQUEST);
    }

    @Bean
    public JwtRefreshTokenAuthenticationFilter jwtRefreshTokenAuthenticationFilter() throws Exception {
        return new JwtRefreshTokenAuthenticationFilter(authenticationManager(), jwtAuthenticationEntryPoint(), REGENERATE_TOKEN_REQUEST);
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
}