package com.ogjg.daitgym.config.security.jwt.util;

import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final int MINUTE = 60 * 1000;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;
    private static final int ACCESS_TOKEN_VALID_TIME = MINUTE * 5;
    private static final long REFRESH_TOKEN_VALID_TIME = DAY * 30L;
    private static final String ISSUER = "team_ogjg";
    private static Key SIGNATURE_KEY;
    private static String JWT_SECRET;

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
                    "alg", "HS256",
                    "typ", "JWT"
            ));
        }

        private static Map<String, Object> createClaims(JwtUserClaimsDto jwtUserClaimsDto) {
            return new HashMap<>(Map.of(
                    "iss", ISSUER,
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
}