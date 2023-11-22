package com.ogjg.daitgym.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EditNicknameRequest {

    @Pattern(regexp = "^[a-zA-Z0-9_]{3,11}$", message = "닉네임은 영문, 숫자, _ 만 사용 가능하며, 길이는 3자 이상 11자 이하여야 합니다.")
    private String nickname;

    @Builder
    public EditNicknameRequest(String nickname) {
        this.nickname = nickname;
    }
}
