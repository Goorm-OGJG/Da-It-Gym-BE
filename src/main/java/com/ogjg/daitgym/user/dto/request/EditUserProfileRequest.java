package com.ogjg.daitgym.user.dto.request;

import com.ogjg.daitgym.domain.HealthClub;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EditUserProfileRequest {

    @Pattern(regexp = "^[a-zA-Z0-9_]{3,11}$", message = "닉네임은 영문, 숫자, _ 만 사용 가능하며, 길이는 3자 이상 11자 이하여야 합니다.")
    private String nickname;
    private String introduction;
    private String gymName;
    private String preferredSplit;

    public EditUserProfileRequest(String nickname, String introduction, String gymName, String preferredSplit) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.gymName = gymName;
        this.preferredSplit = preferredSplit;
    }

    // todo : 위치 정보 추가 후 정보 갱신
    public HealthClub toHealthClub() {
        return HealthClub.builder()
                .name(gymName)
                .build();
    }
}
