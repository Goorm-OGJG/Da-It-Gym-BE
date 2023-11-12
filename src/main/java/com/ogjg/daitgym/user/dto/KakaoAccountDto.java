package com.ogjg.daitgym.user.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class KakaoAccountDto {

    private Long id;
    private KakaoAccount kakao_account;
    @Data
    public static class KakaoAccount {

        private String email;
        private Profile profile;

        @Data
        public static class Profile {

            private String nickname;
        }
    }
}