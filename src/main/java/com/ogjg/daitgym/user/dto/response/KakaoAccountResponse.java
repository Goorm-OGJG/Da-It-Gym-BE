package com.ogjg.daitgym.user.dto.response;

import lombok.Getter;

@Getter
public class KakaoAccountResponse {

    private Long id;
    private KakaoAccount kakao_account;
    @Getter
    public static class KakaoAccount {

        private String email;
        private Profile profile;

        @Getter
        public static class Profile {

            private String nickname;
        }
    }
}