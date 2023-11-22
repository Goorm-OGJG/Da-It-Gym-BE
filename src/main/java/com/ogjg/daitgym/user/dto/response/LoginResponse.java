package com.ogjg.daitgym.user.dto.response;

import com.ogjg.daitgym.domain.Role;
import com.ogjg.daitgym.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import static com.ogjg.daitgym.user.constants.UserConstants.*;

@Getter
@NoArgsConstructor
@Component
public class LoginResponse {

    private boolean isAlreadyJoined;

    private boolean isDeleted;

    private String nickname;

    private String userProfileImgUrl;

    private String preferredSplit;

    private String introduction;

    private String healthClubName;

    private String role;

    @Builder
    public LoginResponse(String nickname, String userProfileImgUrl, String preferredSplit, String introduction, String healthClubName, boolean isAlreadyJoined, String role, boolean isDeleted) {
        this.nickname = nickname;
        this.userProfileImgUrl = userProfileImgUrl;
        this.preferredSplit = preferredSplit;
        this.introduction = introduction;
        this.healthClubName = healthClubName;
        this.isAlreadyJoined = isAlreadyJoined;
        this.role = role;
        this.isDeleted = isDeleted;
    }
    
    public static LoginResponse newUserResponse(String tempNickname) {
        return defaultUserInfoBuilder(tempNickname)
                .isAlreadyJoined(NOT_ALREADY_JOINED)
                .isDeleted(NOT_DELETED)
                .build();
    }

    private static LoginResponseBuilder defaultUserInfoBuilder(String tempNickname) {
        return LoginResponse.builder()
                .nickname(tempNickname)
                .userProfileImgUrl(AWS_DEFAULT_PROFILE_IMG_URL)
                .preferredSplit(DEFAULT_PREFERRED_SPLIT)
                .introduction(DEFAULT_INTRODUCTION)
                .healthClubName(DEFAULT_HEALTH_CLUB_NAME)
                .role(Role.USER.getTitle());
    }

    public static LoginResponse deletedUserResponse(User deletedUser) {
        return findUserInfoBuilder(deletedUser)
                .isAlreadyJoined(ALREADY_JOINED)
                .isDeleted(DELETED)
                .build();
    }

    public static LoginResponse existUserResponse(User existUser) {
        return findUserInfoBuilder(existUser)
                .isAlreadyJoined(ALREADY_JOINED)
                .isDeleted(NOT_DELETED)
                .build();
    }

    private static LoginResponseBuilder findUserInfoBuilder(User user) {
        return LoginResponse.builder()
                .nickname(user.getNickname())
                .userProfileImgUrl(user.getImageUrl())
                .preferredSplit(user.getPreferredSplit().getTitleOrDefault())
                .introduction(user.getIntroduction())
                .healthClubName(user.getHealthClub().getName())
                .role(user.getRole().getTitleOrDefault());
    }
}

