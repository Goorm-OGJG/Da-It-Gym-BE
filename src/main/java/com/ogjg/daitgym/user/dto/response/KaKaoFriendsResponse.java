package com.ogjg.daitgym.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KaKaoFriendsResponse {

    private List<KaKaoFriendResponseDto> elements = new ArrayList<>();

    public KaKaoFriendsResponse(List<KaKaoFriendResponseDto> elements) {
        this.elements = elements;
    }
}
