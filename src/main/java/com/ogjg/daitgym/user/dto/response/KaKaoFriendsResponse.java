package com.ogjg.daitgym.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class KaKaoFriendsResponse {

    private List<KaKaoFriendResponseDto> elements = new ArrayList<>();
    private Integer total_count;

    public KaKaoFriendsResponse(List<KaKaoFriendResponseDto> elements) {
        this.elements = elements;
    }
}
