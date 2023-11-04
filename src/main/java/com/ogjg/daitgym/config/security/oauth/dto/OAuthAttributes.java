package com.ogjg.daitgym.config.security.oauth.dto;

import com.ogjg.daitgym.domain.Role;
import com.ogjg.daitgym.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class OAuthAttributes implements Serializable {
    private Map<String, Object> attributes;

    private String nameAttributeKey;

    private String name;

    private String nickname;

    private String email;

    private String picture;

    private boolean isAlreadyJoined;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String,Object> attributes) {
        return ofKakao("id", attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String,Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname")) // 이름은 동의없이 못받는다.
                .nickname((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .build();
    }

    public void setAlreadyJoined(boolean isAlreadyJoined) {
        this.isAlreadyJoined = isAlreadyJoined;
    }
}
