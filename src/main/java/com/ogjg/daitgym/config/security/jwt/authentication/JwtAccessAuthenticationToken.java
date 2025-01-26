package com.ogjg.daitgym.config.security.jwt.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAccessAuthenticationToken extends JwtAuthenticationToken {
    public JwtAccessAuthenticationToken(String jwt) {
        super(jwt);
    }

    public JwtAccessAuthenticationToken(UserDetails principal, String jwt, Collection<? extends GrantedAuthority> authorities) {
        super(principal, jwt, authorities);
    }
}