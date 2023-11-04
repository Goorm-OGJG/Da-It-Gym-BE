package com.ogjg.daitgym.config.security.oauth;

import com.ogjg.daitgym.config.security.OAuth2JwtUserDetails;
import com.ogjg.daitgym.config.security.oauth.dto.OAuthAttributes;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        OAuthAttributes attributes = getOAuthAttributes(userRequest, oAuth2User);

        User user = saveOrUpdate(attributes);

        return new OAuth2JwtUserDetails(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes,
                isAlreadyJoinedUser(attributes.getEmail())
        );
    }

    private static OAuthAttributes getOAuthAttributes(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        return OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
    }

    private boolean isAlreadyJoinedUser(String email) {
        return userRepository.findByEmail(email)
                .isPresent();
    }

    // todo : 다른 소셜로그인이 추가된다 가장하면 registrationId를 사용해야한다.
    // todo : 싱크 관련 요구사항 종합 필요
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
