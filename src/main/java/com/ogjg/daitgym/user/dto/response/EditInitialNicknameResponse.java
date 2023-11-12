package com.ogjg.daitgym.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EditInitialNicknameResponse {

    private String nickname;

    public EditInitialNicknameResponse(String nickname) {
        this.nickname = nickname;
    }

    public static EditInitialNicknameResponse of(String nickname) {
        return new EditInitialNicknameResponse(nickname);
    }
}
