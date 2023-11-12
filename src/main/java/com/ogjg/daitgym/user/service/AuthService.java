package com.ogjg.daitgym.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.user.dto.LoginResponseDto;
import com.ogjg.daitgym.domain.Role;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.dto.KakaoAccountDto;
import com.ogjg.daitgym.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Value("${kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URI;

    // 토큰으로 사용자 정보 가져오기 -> 처음 로그인인지 체크하고 로그인 응답 생성
    @Transactional
    public LoginResponseDto kakaoLogin(String kakaoAccessToken, HttpServletResponse servletResponse) {
        return getKakaoInfo(kakaoAccessToken, servletResponse);
    }

    // 어세스 토큰으로 사용자 정보 가져오기 -> 첫 로그인이라면 가입처리
    @Transactional
    public LoginResponseDto getKakaoInfo(String kakaoAccessToken, HttpServletResponse servletResponse) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 후 response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                KAKAO_USER_INFO_URI, // "https://kapi.kakao.com/v2/user/me"
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        // JSON Parsing (-> kakaoAccountDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoAccountDto kakaoAccountDto = null;

        try {
            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // todo 탈퇴해서 아이디가 남아있는 회원의 처리가 추가되어야 한다.
        // 회원가입 처리 -> 존재하면 정보 가져오기, 존재하지 않으면 새로 저장
        String kakaoEmail = kakaoAccountDto.getKakao_account().getEmail();
        User existUser = userRepository.findByEmailIncludingDeleted(kakaoEmail)
                .orElse(null);

        boolean isAlreadyJoined = false;
        boolean isDeleted = false;
        String tempNickname = UUID.randomUUID().toString();

        // 첫 가입
        if (existUser == null) {
            isAlreadyJoined = false;
            isDeleted = false;

            // 가입 처리
            User user = User.builder()
                    .email(kakaoAccountDto.getKakao_account().getEmail())
                    .nickname(tempNickname)
                    .role(Role.USER)
                    .build();

            userRepository.save(user);

            JwtUserClaimsDto jwtUserClaimsDto = JwtUserClaimsDto.builder()
                    .email(kakaoEmail)
                    .role(Role.USER)
                    .build();
            addTokensInHeader(servletResponse, jwtUserClaimsDto);

            return LoginResponseDto.builder()
                    .isAlreadyJoined(isAlreadyJoined)
                    .isDeleted(isDeleted)
                    .isAdmin(false)
                    .userImg("defaultUrl")
                    .nickname(tempNickname)
                    .build();

        // todo : 가입했다 탈퇴한 회원 처리
        } else if (existUser.isDeleted() == true){
            isAlreadyJoined = true;
            isDeleted = true;

            return LoginResponseDto.builder()
                    .isAlreadyJoined(isAlreadyJoined)
                    .isDeleted(isDeleted)
                    .isAdmin(false)
                    .userImg("defaultUrl")
                    .nickname(tempNickname)
                    .build();

        // 가입한 회원 -> 유저정보를 불러온다.
        } else {
            isAlreadyJoined = true;
            isDeleted = false;

            JwtUserClaimsDto jwtUserClaimsDto = JwtUserClaimsDto.builder()
                    .email(kakaoEmail)
                    .role(Role.USER)
                    .build();
            addTokensInHeader(servletResponse, jwtUserClaimsDto);

            return LoginResponseDto.builder()
                    .isAlreadyJoined(isAlreadyJoined)
                    .isDeleted(isDeleted)
                    .isAdmin(existUser.isAdmin())
                    .userImg(existUser.getImageUrl())
                    .nickname(existUser.getNickname())
                    .build();
        }
    }
    private void addTokensInHeader(HttpServletResponse response, JwtUserClaimsDto jwtUserClaimsDto) {
        String accessToken = TokenGenerator.generateAccessToken(jwtUserClaimsDto);
        String refreshToken = TokenGenerator.generateRefreshToken(jwtUserClaimsDto);

        response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + accessToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Set-Cookie", createRefreshTokenCookie(refreshToken).toString());
        response.setCharacterEncoding("UTF-8");
    }

    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(60 * 60 * 24 * 30)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }

    // 인가코드 보내서 토큰 받아오기, KakaoDto에 바인딩을 직접해줘서 반환한다.
    private void addTokenInHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + accessToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHARSET_UTF_8);
    }

    @Transactional
    public KakaoTokenDto getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Http Response Body 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", KAKAO_CLIENT_ID); // 카카오 Dev 앱 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URI); // 카카오 Dev redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 받은 인가 코드값
        params.add("client_secret", KAKAO_CLIENT_SECRET); // 카카오 Dev 카카오 로그인 Client Secret

        // 헤더와 바디 합치기 위해 Http Entity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 카카오로부터 Access token 받아오기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                KAKAO_TOKEN_URI, // "https://kauth.kakao.com/oauth/token"
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON Parsing (-> KakaoTokenDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto;
    }

    @Data
    public static class KakaoTokenDto {

        private String access_token;
        private String token_type;
        private String refresh_token;
        private String id_token;
        private int expires_in;
        private int refresh_token_expires_in;
        private String scope;
    }
}
