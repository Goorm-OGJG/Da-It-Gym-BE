package com.ogjg.daitgym.config.security;

import com.ogjg.daitgym.config.security.oauth.dto.OAuthAttributes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
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
        return "";
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
}
