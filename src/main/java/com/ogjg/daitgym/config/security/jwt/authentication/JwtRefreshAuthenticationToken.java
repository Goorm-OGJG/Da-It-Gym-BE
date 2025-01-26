package com.ogjg.daitgym.config.security.jwt.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtRefreshAuthenticationToken extends JwtAuthenticationToken {
    public JwtRefreshAuthenticationToken(String jwt) {
        super(jwt);
    }

    public JwtRefreshAuthenticationToken(UserDetails principal, String jwt, Collection<? extends GrantedAuthority> authorities) {
        super(principal, jwt, authorities);
    }
}