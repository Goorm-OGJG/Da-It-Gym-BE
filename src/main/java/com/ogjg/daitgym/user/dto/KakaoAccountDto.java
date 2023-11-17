package com.ogjg.daitgym.user.dto;

import lombok.Getter;

@Getter
public class KakaoAccountDto {

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