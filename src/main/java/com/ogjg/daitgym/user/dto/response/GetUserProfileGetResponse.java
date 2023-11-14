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
    private int journalCount;
    private int followerCount;
    private int followingCount;

    @Builder
    public GetUserProfileGetResponse(String nickname, String preferredSplit, String userProfileImgUrl, String introduction, String healthClubName, int journalCount, int followerCount, int followingCount) {
        this.nickname = nickname;
        this.preferredSplit = preferredSplit;
        this.userProfileImgUrl = userProfileImgUrl;
        this.introduction = introduction;
        this.healthClubName = healthClubName;
        this.journalCount = journalCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
}
