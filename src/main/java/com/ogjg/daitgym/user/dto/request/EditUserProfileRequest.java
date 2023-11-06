package com.ogjg.daitgym.user.dto.request;

import com.ogjg.daitgym.domain.HealthClub;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class EditUserProfileRequest {

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
