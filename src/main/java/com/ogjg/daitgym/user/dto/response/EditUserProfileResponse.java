package com.ogjg.daitgym.user.dto.response;

import com.ogjg.daitgym.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EditUserProfileResponse {

    private String nickname;
    private String preferredSplit;
    private String userProfileImgUrl;
    private String introduction;
    private String healthClubName;
    private String role;

    @Builder
    public EditUserProfileResponse(String nickname, String preferredSplit, String userProfileImgUrl, String introduction, String healthClubName, String role) {
        this.nickname = nickname;
        this.preferredSplit = preferredSplit;
        this.userProfileImgUrl = userProfileImgUrl;
        this.introduction = introduction;
        this.healthClubName = healthClubName;
        this.role = role;
    }

    public static EditUserProfileResponse of(User user) {
        return EditUserProfileResponse.builder()
                .nickname(user.getNickname())
                .preferredSplit(user.getPreferredSplit().getTitle())
                .userProfileImgUrl(user.getImageUrl())
                .introduction(user.getIntroduction())
                .healthClubName(user.getHealthClub().getName())
                .role(user.getRole().getTitle())
                .build();
    }
}
