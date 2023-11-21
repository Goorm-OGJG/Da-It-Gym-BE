package com.ogjg.daitgym.domain;


import com.ogjg.daitgym.user.dto.response.KakaoAccountResponse;
import com.ogjg.daitgym.user.dto.response.KakaoTokenResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class UserAuthentication {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long provider_id;

    private String provider_name;

    @OneToOne(fetch = LAZY)
    private User user;

    private Long uuid;

    private String accessToken;

    private String refreshToken;

    @Builder
    public UserAuthentication(Long id, Long provider_id, String provider_name, User user, Long uuid, String accessToken, String refreshToken) {
        this.id = id;
        this.provider_id = provider_id;
        this.provider_name = provider_name;
        this.user = user;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static UserAuthentication of(KakaoTokenResponse kakaoTokenResponse, KakaoAccountResponse kakaoAccountResponse) {
        return UserAuthentication.builder()
                .accessToken(kakaoTokenResponse.getAccess_token())
                .refreshToken(kakaoTokenResponse.getRefresh_token())
                .uuid(kakaoAccountResponse.getId())
                .provider_id(kakaoAccountResponse.getId())
                .provider_name("kakao")
                .build();
    }

    public void addUser(User joinedUser) {
        this.user = joinedUser;
    }

    public void updateTokens(String access_token, String refresh_token) {
        this.accessToken = access_token;
        this.refreshToken = refresh_token;
    }
}
