package com.ogjg.daitgym.user.dto.response;

import com.ogjg.daitgym.domain.Role;
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

    @Builder
    public GetUserProfileGetResponse(String nickname, String preferredSplit, String userProfileImgUrl, String introduction, String healthClubName, boolean isFollower, Role role, int journalCount, int followerCount, int followingCount) {
        this.nickname = nickname;
        this.preferredSplit = preferredSplit;
        this.userProfileImgUrl = userProfileImgUrl;
        this.introduction = introduction;
        this.healthClubName = healthClubName;
        this.isFollower = isFollower;
        this.role = role.getTitle();
        this.journalCount = journalCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
}
