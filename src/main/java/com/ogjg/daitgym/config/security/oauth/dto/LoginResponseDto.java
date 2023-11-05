package com.ogjg.daitgym.config.security.oauth.dto;

import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class LoginResponseDto {
    private String email;

    private String nickname;

    private String userImg;

    private String initialRequestUrl;

    private boolean isAlreadyJoined;

    public LoginResponseDto(String email, String nickname, String userImg, String initialRequestUrl, boolean isAlreadyJoined) {
        this.email = email;
        this.nickname = nickname;
        this.userImg = userImg;
        this.initialRequestUrl = initialRequestUrl;
        this.isAlreadyJoined = isAlreadyJoined;
    }

    public static LoginResponseDto of(OAuth2JwtUserDetails oAuth2UserDetails, String initialRequestUrl) {
        return LoginResponseDto.builder()
                .nickname(oAuth2UserDetails.getNickname())
                .email(oAuth2UserDetails.getEmail())
                .userImg("default")
                .initialRequestUrl(initialRequestUrl)
                .isAlreadyJoined(oAuth2UserDetails.isAlreadyJoined())
                .build();
    }
}

