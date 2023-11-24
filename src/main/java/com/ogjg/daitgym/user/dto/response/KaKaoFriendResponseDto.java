package com.ogjg.daitgym.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KaKaoFriendResponseDto {

    private String nickName;
    private String imageUrl;
    private String intro;
    private int score;

    @Builder
    public KaKaoFriendResponseDto(String nickName, String imageUrl, String intro, int score) {
        this.nickName = nickName;
        this.imageUrl = imageUrl;
        this.intro = intro;
        this.score = score;
    }
}
