package com.ogjg.daitgym.config.security.jwt.util;

import com.ogjg.daitgym.chat.exception.UnauthorizedException;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    // todo : 상수 처리
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String SPACE = " ";

    public static final String CHARSET_UTF_8 = "UTF-8";

    private static String JWT_SECRET;

    private static final String ALGORITHM_KEY = "alg";

    private static final String ALGORITHM_HS256 = "HS256";

    private static final String TYPE_KEY = "typ";

    private static final String TYPE = "JWT";

    private static final String ISSUER_KEY = "iss";

    private static final String ISSUER = "team_ogjg";

    public static final String HEADER_AUTHORIZATION = "Authorization";

    private static final int MINUTE = 60 * 1000;

    private static final int HOUR = MINUTE * 60;

    private static final int DAY = HOUR * 24;

    private static final int ACCESS_TOKEN_VALID_TIME = MINUTE * 5;

    private static final long REFRESH_TOKEN_VALID_TIME = DAY * 30L;

    private static Key SIGNATURE_KEY;

    @Value("${jwt.secret}")
    private void setJwtSecret(String jwtSecret) {
        JWT_SECRET = jwtSecret;
    }

    public static class TokenGenerator {
        public static String generateAccessToken(JwtUserClaimsDto jwtUserClaimsDto) {
            return Jwts.builder()
                    .setHeader(createHeader())
                    .setClaims(createClaims(jwtUserClaimsDto))
                    .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME))
                    .signWith(generateKey())
                    .compact();
        }

        public static String generateRefreshToken(JwtUserClaimsDto userClaimsDto) {
            return Jwts.builder()
                    .setHeader(createHeader())
                    .setClaims(createClaims(userClaimsDto))
                    .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
                    .signWith(generateKey())
                    .compact();
        }

        private static Map<String, Object> createHeader() {
            return new HashMap<>(Map.of(
                    ALGORITHM_KEY, ALGORITHM_HS256,
                    TYPE_KEY, TYPE
            ));
        }

        private static Map<String, Object> createClaims(JwtUserClaimsDto jwtUserClaimsDto) {
            return new HashMap<>(Map.of(
                    ISSUER_KEY, ISSUER,
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

    public static class TokenVerifier {
        public static Claims verifyTokenAndGetClaims(String jwt) {
            Jws<Claims> claimsJws = parseAndVerify(jwt);
            return claimsJws.getBody();
        }

        private static Jws<Claims> parseAndVerify(String token) {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(TokenGenerator.generateKey())
                    .build()
                    .parseClaimsJws(token);
            return claimsJws;
        }
    }

    public static class TokenValidator {
        public static void validateExpiration(Claims claims) {
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("토큰이 만료되었습니다.");
            }
        }

        public static void validateIssuer(Claims claims) {
            if (!ISSUER.equals(claims.get(ISSUER_KEY))) {
                throw new JwtException("Issuer가 일치하지 않습니다.");
            }
        }

        public static void validateHasToken(String jwt) {
            if (jwt == null) {
                throw new JwtException("Token이 존재하지 않습니다.");
            }
        }

        public static void validatePrefix(String jwt) {
            if (jwt != null && !jwt.startsWith(TOKEN_PREFIX)) {
                throw new JwtException("Token의 prefix가 유효하지 않습니다.");
            }
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SIGNATURE_KEY).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw UnauthorizedException.of(e.getClass().getName(), "잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException.of(e.getClass().getName(), "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw UnauthorizedException.of(e.getClass().getName(), "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw UnauthorizedException.of(e.getClass().getName(), "JWT 토큰이 잘못되었습니다.");
        }
    }

    public String getTokenStompHeader(String token) {
        try {
            return getAccessToken(URLDecoder.decode(token, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(
                TOKEN_PREFIX)) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}