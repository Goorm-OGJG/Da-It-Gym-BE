package com.ogjg.daitgym.user.dto;

import com.ogjg.daitgym.domain.ExerciseSplit;
import com.ogjg.daitgym.domain.Role;
import com.ogjg.daitgym.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    public static final String DEFAULT_HEALTHCLUB_NAME = "";
    @Value("${cloud.aws.default.profile-img}")
    private static String AWS_DEFAULT_PROFILE_IMG_URL;
    public static final String DEFAULT_INTRODUCTION = DEFAULT_HEALTHCLUB_NAME;
    public static final String DEFAULT_PREFERRED_SPLIT = ExerciseSplit.ONE_DAY.getTitle();
    private static final boolean ALREADY_JOINED = true;
    private static final boolean NOT_ALREADY_JOINED = true;
    private static final boolean DELETED = true;
    private static final boolean NOT_DELETED = false;

    private boolean isAlreadyJoined;

    private boolean isDeleted;

    private String nickname;

    private String userProfileImgUrl;

    private String preferredSplit;

    private String introduction;

    private String healthClubName;

    private String role;


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
    
    public static LoginResponseDto newUserResponse(String tempNickname) {
        return defaultUserInfoBuilder(tempNickname)
                .isAlreadyJoined(NOT_ALREADY_JOINED)
                .isDeleted(NOT_DELETED)
                .build();
    }

    private static LoginResponseDtoBuilder defaultUserInfoBuilder(String tempNickname) {
        return LoginResponseDto.builder()
                .nickname(tempNickname)
                .userProfileImgUrl(AWS_DEFAULT_PROFILE_IMG_URL)
                .preferredSplit(DEFAULT_PREFERRED_SPLIT)
                .introduction(DEFAULT_INTRODUCTION)
                .healthClubName(DEFAULT_HEALTHCLUB_NAME)
                .role(Role.USER.getTitle());
    }

    public static LoginResponseDto deletedUserResponse(User deletedUser) {
        return findUserInfoBuilder(deletedUser)
                .isAlreadyJoined(ALREADY_JOINED)
                .isDeleted(DELETED)
                .build();
    }

    public static LoginResponseDto existUserResponse(User existUser) {
        return findUserInfoBuilder(existUser)
                .isAlreadyJoined(ALREADY_JOINED)
                .isDeleted(NOT_DELETED)
                .build();
    }

    private static LoginResponseDtoBuilder findUserInfoBuilder(User user) {
        return LoginResponseDto.builder()
                .nickname(user.getNickname())
                .userProfileImgUrl(user.getImageUrl())
                .preferredSplit(user.getPreferredSplit().getTitleOrDefault())
                .introduction(user.getIntroduction())
                .healthClubName(user.getHealthClub().getName())
                .role(user.getRole().getTitleOrDefault());
    }
}

