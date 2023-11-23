package com.ogjg.daitgym.user.dto.response;

import com.ogjg.daitgym.user.dto.request.KaKaoFriendRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class KaKaoFriendsResponse {

    private List<KaKaoFriendRequest> friends = new ArrayList<>();

    public KaKaoFriendsResponse(List<KaKaoFriendRequest> friends) {
        this.friends = friends;
    }
}
