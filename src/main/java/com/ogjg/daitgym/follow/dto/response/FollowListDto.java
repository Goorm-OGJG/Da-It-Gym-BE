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

    @QueryProjection
    public FollowListDto(String imageUrl, String nickname, String intro, int score) {
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.intro = intro;
        this.score = score;
    }
}
