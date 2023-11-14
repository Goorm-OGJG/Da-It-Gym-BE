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

    private String userProfileImgUrl;

    private String preferredSplit;

    private String introduction;

    private String healthClubName;

    private boolean isAlreadyJoined;

    private String role;

    private boolean isDeleted;

    @Builder
    public LoginResponseDto(String nickname, String userProfileImgUrl, String preferredSplit, String introduction, String healthClubName, boolean isAlreadyJoined, String role, boolean isDeleted) {
        this.nickname = nickname;
        this.userProfileImgUrl = userProfileImgUrl;
        this.preferredSplit = preferredSplit;
        this.introduction = introduction;
        this.healthClubName = healthClubName;
        this.isAlreadyJoined = isAlreadyJoined;
        this.role = role;
        this.isDeleted = isDeleted;
    }

    public static LoginResponseDto from(OAuth2JwtUserDetails oAuth2UserDetails) {
        return LoginResponseDto.builder()
                .nickname(UUID.randomUUID().toString())
                .userProfileImgUrl("default")
                .isAlreadyJoined(oAuth2UserDetails.isAlreadyJoined())
                .build();
    }
}

