package com.ogjg.daitgym.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EditNicknameRequest {

    private String nickname;

    @Builder
    public EditNicknameRequest(String nickname) {
        this.nickname = nickname;
    }
}
