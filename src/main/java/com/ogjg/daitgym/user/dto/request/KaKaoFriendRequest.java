package com.ogjg.daitgym.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KaKaoFriendRequest {

    private String nickName;
    private String profileImageURL;
    private String thumbnailURL;
}
