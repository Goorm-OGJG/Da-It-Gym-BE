package com.ogjg.daitgym.follow.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FollowListDto {

    private String imageUrl;
    private String nickname;
    private String intro;
    private int score;

    public void putLatestInbodyScore(int score){
        this.score = score;
    }

    @QueryProjection
    public FollowListDto(String imageUrl, String nickname, String intro) {
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.intro = intro;
    }
}
