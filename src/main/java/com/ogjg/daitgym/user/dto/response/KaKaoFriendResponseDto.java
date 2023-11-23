package com.ogjg.daitgym.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KaKaoFriendResponseDto {

    private Long id;
    private String uuid;
    private String nickName;
    private String profileImageURL;

    public void putUserData(
            String nickName, String profileImageURL
    ){
        this.nickName = nickName;
        this.profileImageURL = profileImageURL;
    }
}
