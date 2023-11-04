package com.ogjg.daitgym.config.security;

import com.ogjg.daitgym.config.security.oauth.CustomOAuth2UserService;
import com.ogjg.daitgym.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

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
}
