package com.ogjg.daitgym.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class GetUserProfileGetResponse {

    private String healthClubName;
    private int journalCount;
    private int followerCount;
    private int followingCount;

    @Builder
    public GetUserProfileGetResponse(String healthClubName, int journalCount, int followerCount, int followingCount) {
        this.healthClubName = healthClubName;
        this.journalCount = journalCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public static GetUserProfileGetResponse of(int journalCount, int followerCount, int followingCount, String healthClubName) {
        return GetUserProfileGetResponse.builder()
                .healthClubName(healthClubName)
                .journalCount(journalCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
