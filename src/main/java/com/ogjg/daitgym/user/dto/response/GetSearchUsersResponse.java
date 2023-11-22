package com.ogjg.daitgym.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Getter
@NoArgsConstructor(access = PROTECTED)
public class GetSearchUsersResponse {

    private List<GetSearchUserResponse> userResponses;
    private boolean hasNext;

    public GetSearchUsersResponse(List<GetSearchUserResponse> userResponses, boolean hasNext) {
        this.userResponses = userResponses;
        this.hasNext = hasNext;
    }


    public static GetSearchUsersResponse from(List<GetSearchUserResponse> searchUsersResponse, boolean hasNext) {
        return new GetSearchUsersResponse(searchUsersResponse, hasNext);
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class GetSearchUserResponse {
        private String userProfileImageUrl;
        private String nickname;
        private String introduction;
        private int inbodyScore;

        @Builder
        public GetSearchUserResponse(String userProfileImageUrl, String nickname, String introduction, int inbodyScore) {
            this.userProfileImageUrl = userProfileImageUrl;
            this.nickname = nickname;
            this.introduction = introduction;
            this.inbodyScore = inbodyScore;
        }
    }


}
