package com.ogjg.daitgym.config.security.jwt.util;

import com.ogjg.daitgym.config.security.jwt.authentication.JwtAccessAuthenticationToken;
import com.ogjg.daitgym.config.security.jwt.authentication.JwtRefreshAuthenticationToken;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.config.security.jwt.exception.RefreshTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ogjg.daitgym.config.security.jwt.constants.JwtConstants.*;

@Component
public class JwtUtils {

    private static String JWT_SECRET;
    private static Key SIGNATURE_KEY;

    @Value("${jwt.secret}")
    private void setJwtSecret(String jwtSecret) {
        JWT_SECRET = jwtSecret;
    }

    public static class Generator {

        public static String generateAccessToken(JwtUserClaimsDto jwtUserClaimsDto) {
            return Jwts.builder()
                    .setHeader(createHeader(ACCESS_TOKEN.TOKEN_TYPE))
                    .setClaims(createClaims(jwtUserClaimsDto))
                    .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN.VALID_TIME))
                    .signWith(generateKey())
                    .compact();
        }

        public static String generateRefreshToken(JwtUserClaimsDto userClaimsDto) {
            return Jwts.builder()
                    .setHeader(createHeader(REFRESH_TOKEN.TOKEN_TYPE))
                    .setClaims(createClaims(userClaimsDto))
                    .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN.VALID_TIME))
                    .signWith(generateKey())
                    .compact();
        }

        private static Map<String, Object> createHeader(String tokenType) {
            return new HashMap<>(Map.of(
                    HEADER.KEY.ALGORITHM, HEADER.VALUE.ALGORITHM_HS256,
                    HEADER.KEY.TYPE, HEADER.VALUE.TYPE_JWT,
                    HEADER.KEY.TOKEN_TYPE, tokenType
            ));
        }

        private static Map<String, Object> createClaims(JwtUserClaimsDto jwtUserClaimsDto) {
            return new HashMap<>(Map.of(
                    CLAIM.KEY.ISSUER, CLAIM.VALUE.ISSUER,
                    "nickname", jwtUserClaimsDto.getNickname(),
                    "email", jwtUserClaimsDto.getEmail(),
                    "role", jwtUserClaimsDto.getRole().getKey()
            ));
        }

        private static Key generateKey() {
            if (SIGNATURE_KEY == null) {
                byte[] byteSecretKey = JWT_SECRET.getBytes(StandardCharsets.UTF_8);
                SIGNATURE_KEY = new SecretKeySpec(byteSecretKey, SignatureAlgorithm.HS256.getJcaName());
            }
            return SIGNATURE_KEY;
        }

    }

    public static class Verifier {

        public static Jws<Claims> verifyTokenInStomp(StompHeaderAccessor headerAccessor) {
            String jwt = getAccessTokenFrom(headerAccessor);
            return verifyToken(jwt, new JwtAccessAuthenticationToken(jwt));
        }

        public static Jws<Claims> verifyToken(String jwt, Authentication authentication) {
            Jws<Claims> claimsJws = verifyToken(jwt);

            Validator.validateIssuer(claimsJws);
            Validator.validateTokenType(claimsJws, authentication);

            return claimsJws;
        }

        public static Jws<Claims> verifyToken(String jwt) {
            try {
                return parseToken(jwt);
            } catch (SecurityException | MalformedJwtException e) {
                throw new JwtException("잘못된 JWT 토큰입니다.");
            } catch (ExpiredJwtException e) {
                throw new JwtException("만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                throw new JwtException("지원되지 않는 JWT 토큰입니다.");
            } catch (IllegalArgumentException e) {
                throw new JwtException("JWT 토큰이 잘못되었습니다.");
            }
        }
    }

    public static class Validator {

        public static void validateHasToken(String jwt) {
            if (jwt == null) {
                throw new JwtException("Token이 존재하지 않습니다.");
            }
        }

        public static void validatePrefix(String jwt) {
            if (jwt != null && !jwt.startsWith(ACCESS_TOKEN.PREFIX)) {
                throw new JwtException("Token의 prefix가 유효하지 않습니다.");
            }
        }

        public static void validateIssuer(Jws<Claims> claimsJws) {
            Object expectedIssuer = claimsJws.getBody().get(CLAIM.KEY.ISSUER);

            if (!CLAIM.VALUE.ISSUER.equals(expectedIssuer)) {
                throw new JwtException("Issuer가 일치하지 않습니다.");
            }
        }

        public static void validateTokenType(Jws<Claims> claimsJws, Authentication authentication) {
            String actualTokenType = findTokenType(authentication);
            Object expectedTokenType = claimsJws.getHeader().get(HEADER.KEY.TOKEN_TYPE);

            if (!actualTokenType.equals(expectedTokenType)) {
                throw new JwtException("Token의 type이 일치하지 않습니다.");
            }
        }

        private static String findTokenType(Authentication authentication) {
            if (authentication instanceof JwtAccessAuthenticationToken) {
                return ACCESS_TOKEN.TOKEN_TYPE;
            }
            else if (authentication instanceof JwtRefreshAuthenticationToken) {
                return REFRESH_TOKEN.TOKEN_TYPE;
            }
            else {
                throw new JwtException("존재하지 않는 Token type입니다.");
            }
        }
    }

    public static String getAccessTokenFrom(StompHeaderAccessor headerAccessor) {
        String headerValue = headerAccessor.getFirstNativeHeader("Authentication");

        try {
            return getAccessTokenFrom(URLDecoder.decode(headerValue, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getAccessTokenFrom(HttpServletRequest request) {
        String headerValue = request.getHeader(ACCESS_TOKEN.HTTP_HEADER_AUTHORIZATION);
        return getAccessTokenFrom(headerValue);
    }

    public static String getAccessTokenFrom(String authHeaderValue) {
        Validator.validateHasToken(authHeaderValue);
        Validator.validatePrefix(authHeaderValue);

        return authHeaderValue.substring(ACCESS_TOKEN.PREFIX.length());
    }

    public static String getRefreshTokenFrom(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(RefreshTokenException::new)
                .getValue();
    }

    public static ResponseCookie createExpiredRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN.COOKIE.NAME, null)
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }

    public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN.COOKIE.NAME, refreshToken)
                .maxAge(REFRESH_TOKEN.MAX_AGE)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }

    public static String extractEmail(String authHeaderValue) {
        String jwt = getAccessTokenFrom(authHeaderValue);
        return extractEmail(parseToken(jwt));
    }

    public static String extractEmail(StompHeaderAccessor headerAccessor) {
        String jwt = getAccessTokenFrom(headerAccessor);
        return extractEmail(parseToken(jwt));
    }

    public static String extractEmail(Jws<Claims> claimsJws) {
        return claimsJws.getBody().get("email", String.class);
    }

    private static Jws<Claims> parseToken(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(Generator.generateKey())
                .build()
                .parseClaimsJws(jwt);
    }
}