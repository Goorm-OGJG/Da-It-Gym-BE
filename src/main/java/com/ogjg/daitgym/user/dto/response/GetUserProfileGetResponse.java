package com.ogjg.daitgym.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class GetUserProfileGetResponse {

    private String nickname;
    private String preferredSplit;
    private String userProfileImgUrl;
    private String introduction;
    private String healthClubName;
    private boolean isFollower;
    private String role;
    private int journalCount;
    private int followerCount;
    private int followingCount;
    private boolean isMyProfile;
    private boolean submitTrainerQualification;

    @Builder
    public GetUserProfileGetResponse(String nickname, String preferredSplit, String userProfileImgUrl, String introduction, String healthClubName, boolean isFollower, String role, int journalCount, int followerCount, int followingCount, boolean isMyProfile, boolean submitTrainerQualification) {
        this.nickname = nickname;
        this.preferredSplit = preferredSplit;
        this.userProfileImgUrl = userProfileImgUrl;
        this.introduction = introduction;
        this.healthClubName = healthClubName;
        this.isFollower = isFollower;
        this.role = role;
        this.journalCount = journalCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.isMyProfile = isMyProfile;
        this.submitTrainerQualification = submitTrainerQualification;
    }
}
