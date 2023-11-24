package com.ogjg.daitgym.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class KaKaoFriendsRequest {

    private List<KaKaoFriendsRequestDto> elements = new ArrayList<>();
    private Integer total_count;

    public KaKaoFriendsRequest(List<KaKaoFriendsRequestDto> elements) {
        this.elements = elements;
    }
}