package com.ogjg.daitgym.user.dto;

import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
public class LoginResponseDto {

    private String nickname;

    private String userImg;

    private String preferredSplit;

    private boolean isAlreadyJoined;

    private boolean isAdmin;

    private boolean isDeleted;

    @Builder
    public LoginResponseDto(String nickname, String userImg, String preferredSplit, boolean isAlreadyJoined, boolean isAdmin, boolean isDeleted) {
        this.nickname = nickname;
        this.userImg = userImg;
        this.preferredSplit = preferredSplit;
        this.isAlreadyJoined = isAlreadyJoined;
        this.isAdmin = isAdmin;
        this.isDeleted = isDeleted;
    }

    public static LoginResponseDto from(OAuth2JwtUserDetails oAuth2UserDetails) {
        return LoginResponseDto.builder()
                .nickname(UUID.randomUUID().toString())
                .userImg("default")
                .isAlreadyJoined(oAuth2UserDetails.isAlreadyJoined())
                .build();
    }
}

