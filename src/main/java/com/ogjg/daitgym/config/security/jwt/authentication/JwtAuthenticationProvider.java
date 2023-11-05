package com.ogjg.daitgym.config.security.jwt.authentication;

import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.TokenValidator;
import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.TokenVerifier;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwt = (String) authentication.getCredentials();

        Claims claims = TokenVerifier.verifyTokenAndGetClaims(jwt);

        TokenValidator.validateIssuer(claims);
        TokenValidator.validateExpiration(claims);

        OAuth2JwtUserDetails userDetails = new OAuth2JwtUserDetails(JwtUserClaimsDto.from(claims));
        return new JwtAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
