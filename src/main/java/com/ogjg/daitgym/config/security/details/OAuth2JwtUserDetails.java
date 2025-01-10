package com.ogjg.daitgym.config.security.details;

import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class OAuth2JwtUserDetails extends DefaultOAuth2User implements UserDetails {

    private final OAuthAttributes oAuthAttributes;

    public OAuth2JwtUserDetails(
            Collection<? extends GrantedAuthority> authorities,
            OAuthAttributes oAuthAttributes,
            boolean alreadyJoined
    ) {
        super(authorities, oAuthAttributes.getAttributes(), oAuthAttributes.getNameAttributeKey());
        this.oAuthAttributes = oAuthAttributes;
        this.oAuthAttributes.setAlreadyJoined(alreadyJoined);
    }

    public OAuth2JwtUserDetails(JwtUserClaimsDto userClaimsDto) {
        super(Collections.singleton(
                        new SimpleGrantedAuthority(userClaimsDto.getRole().getKey())),
                Map.of("name", ""),
                "name");
        this.oAuthAttributes = OAuthAttributes.builder()
                .email(userClaimsDto.getEmail())
                .nickname(userClaimsDto.getNickname())
                .build();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return super.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return getName();
    }

    public String getNickname() {
        return oAuthAttributes.getNickname();
    }

    public String getEmail() {
        return oAuthAttributes.getEmail();
    }

    public boolean isAlreadyJoined() {
        return oAuthAttributes.isAlreadyJoined();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public Role findAnyFirstRole() {
        return getAuthorities().stream()
                .map((grantedAuthority) -> Role.fromKey(grantedAuthority.getAuthority()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("권한이 존재하지 않습니다.")); // todo: 중복적인 검증 로직
    }
}
