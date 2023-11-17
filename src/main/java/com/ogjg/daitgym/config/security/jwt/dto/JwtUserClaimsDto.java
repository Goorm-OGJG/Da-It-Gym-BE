package com.ogjg.daitgym.config.security.jwt.dto;

import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.domain.Role;
import com.ogjg.daitgym.domain.User;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class JwtUserClaimsDto {

    private final String email;

    private final String nickname;

    private final Role role;

    public static JwtUserClaimsDto from(OAuth2JwtUserDetails userDetails) {
        return JwtUserClaimsDto.builder()
                .nickname(userDetails.getNickname())
                .email(userDetails.getEmail())
                .role(userDetails.findAnyFirstRole())
                .build();
    }

    public static JwtUserClaimsDto from(Claims claims) {
        return JwtUserClaimsDto.builder()
                .nickname((String) claims.get("nickname"))
                .email((String) claims.get("email"))
                .role(Role.fromKey((String) claims.get("role")))
                .build();
    }

    public static JwtUserClaimsDto from(User user) {
        return JwtUserClaimsDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
    }

    public static JwtUserClaimsDto defaultClaimsOf(String kakaoEmail, String tempNickname) {
        return JwtUserClaimsDto.builder()
                .email(kakaoEmail)
                .nickname(tempNickname)
                .role(Role.USER)
                .build();
    }
}

