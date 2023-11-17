package com.ogjg.daitgym.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.domain.ExerciseSplit;
import com.ogjg.daitgym.domain.HealthClub;
import com.ogjg.daitgym.domain.Role;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.dto.response.KakaoAccountResponse;
import com.ogjg.daitgym.user.dto.response.KakaoTokenResponse;
import com.ogjg.daitgym.user.dto.response.LoginResponse;
import com.ogjg.daitgym.user.repository.HealthClubRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.*;
import static com.ogjg.daitgym.user.dto.response.LoginResponse.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final HealthClubRepository healthClubRepository;

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
    public LoginResponse kakaoLogin(String kakaoAccessToken, HttpServletResponse servletResponse) {
        return getKakaoInfo(kakaoAccessToken, servletResponse);
    }

    // 어세스 토큰으로 사용자 정보 가져오기 -> 첫 로그인이라면 가입처리
    @Transactional
    public LoginResponse getKakaoInfo(String kakaoAccessToken, HttpServletResponse servletResponse) {
        KakaoAccountResponse kakaoAccountResponse = requestUserInfoToKakao(kakaoAccessToken);
        return joinOrLoadUser(servletResponse, kakaoAccountResponse.getKakao_account().getEmail());
    }

    // 회원가입 처리 -> 존재하면 정보 가져오기, 존재하지 않으면 새로 저장
    private LoginResponse joinOrLoadUser(HttpServletResponse servletResponse, String kakaoEmail) {
        User existUser = userRepository.findByEmailIncludingDeleted(kakaoEmail)
                .orElse(null);

        // 첫 가입
        if (existUser == null) {
            // 가입 처리
            HealthClub defaultHealthClub = findDefaultHealthClub();
            String tempNickname = generateTempNickname();

            join(kakaoEmail, tempNickname, defaultHealthClub);

            JwtUserClaimsDto claimsDto = JwtUserClaimsDto.defaultClaimsOf(kakaoEmail, tempNickname);
            addTokensInHeader(servletResponse, claimsDto);

            return newUserResponse(tempNickname);

        // 이전에 가입 후 탈퇴한 회원
        } else if (existUser.isDeleted()){

            // todo : 가입했다 탈퇴한 회원 처리 -> 탈퇴해서 아이디가 남아있는 회원의 처리가 추가되어야 한다.

            return deletedUserResponse(existUser);

        // 이전에 이미 가입한 회원 -> 유저정보를 불러온다.
        } else {
            JwtUserClaimsDto claimsDto = JwtUserClaimsDto.from(existUser);
            addTokensInHeader(servletResponse, claimsDto);

            return existUserResponse(existUser);
        }
    }

    private KakaoAccountResponse requestUserInfoToKakao(String kakaoAccessToken) {
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
        KakaoAccountResponse kakaoAccountResponse = null;

        try {
            kakaoAccountResponse = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return kakaoAccountResponse;
    }

    private HealthClub findDefaultHealthClub() {
        List<HealthClub> clubs = healthClubRepository.findByName("");
        HealthClub defaultHealthClub;

        if (clubs.isEmpty()) {
            defaultHealthClub = HealthClub.builder()
                    .name("")
                    .build();
            return healthClubRepository.save(defaultHealthClub);
        }

        return clubs.get(0);
    }

    private static String generateTempNickname() {
        return UUID.randomUUID().toString();
    }

    private void join(String kakaoEmail, String tempNickname, HealthClub defaultHealthClub) {
        User user = User.builder()
                .email(kakaoEmail)
                .nickname(tempNickname)
                .imageUrl(AWS_DEFAULT_PROFILE_IMG_URL)
                .introduction(DEFAULT_INTRODUCTION)
                .preferredSplit(ExerciseSplit.THREE_DAY)
                .role(Role.USER)
                .healthClub(defaultHealthClub)
                .isDeleted(NOT_DELETED)
                .build();

        userRepository.save(user);
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

    @Transactional
    public KakaoTokenResponse getKakaoAccessToken(String code) {
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
        KakaoTokenResponse kakaoTokenResponse = null;
        try {
            kakaoTokenResponse = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenResponse;
    }
}

