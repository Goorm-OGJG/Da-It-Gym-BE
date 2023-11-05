package com.ogjg.daitgym.config.security;

import com.ogjg.daitgym.config.security.jwt.authentication.JwtAuthenticationEntryPoint;
import com.ogjg.daitgym.config.security.jwt.authentication.JwtAuthenticationProvider;
import com.ogjg.daitgym.config.security.jwt.filter.JwtAccessTokenAuthenticationFilter;
import com.ogjg.daitgym.config.security.oauth.CustomOAuth2UserService;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    private final List<String> permitJwtUrlList = new ArrayList<>(
            List.of(
                    "/",
                    "/favicon.ico",
                    "/login/oauth2/code/.*",
                    "/oauth2/authorization/.*",
                    "/api/users/token",
                    "/health",
                    "/ws/.*",
                    "/chat/.*"
            ));

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
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(CorsUtils::isPreFlightRequest)
                                .permitAll()
                                .requestMatchers("/api/admins/**").hasRole(Role.ADMIN.name())
                                .requestMatchers("/api/trainers/**").hasRole(Role.TRAINER.name())
                                .requestMatchers("/api/profiles/**").hasRole(Role.USER.name())
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                )
                .logout(
                        (logout) -> logout.logoutSuccessUrl("/")
                )
                .oauth2Login(
                        (oauth2) -> oauth2
                                .successHandler(oauth2AuthenticationSuccessHandler)
                                .userInfoEndpoint((endPoint) -> endPoint
                                        .userService(customOAuth2UserService)
                                )
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
        return new JwtAccessTokenAuthenticationFilter(authenticationManager(), jwtAuthenticationEntryPoint(), permitJwtUrlList);
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
