package com.ogjg.daitgym.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ogjg.daitgym.config.security.jwt.constants.JwtConstants;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.config.security.jwt.util.JwtUtils;
import com.ogjg.daitgym.domain.*;
import com.ogjg.daitgym.user.dto.response.KakaoAccountResponse;
import com.ogjg.daitgym.user.dto.response.KakaoTokenResponse;
import com.ogjg.daitgym.user.dto.response.LoginResponse;
import com.ogjg.daitgym.user.repository.HealthClubRepository;
import com.ogjg.daitgym.user.repository.UserAuthenticationRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.ogjg.daitgym.config.security.jwt.constants.JwtConstants.ACCESS_TOKEN;
import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.Generator;
import static com.ogjg.daitgym.user.constants.UserConstants.*;
import static com.ogjg.daitgym.user.dto.response.LoginResponse.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
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
    public LoginResponse kakaoLogin(KakaoTokenResponse kakaoTokenResponse, HttpServletResponse servletResponse) {
        return getKakaoInfo(kakaoTokenResponse, servletResponse);
    }

    // 어세스 토큰으로 사용자 정보 가져오기 -> 첫 로그인이라면 가입처리
    @Transactional
    public LoginResponse getKakaoInfo(KakaoTokenResponse kakaoTokenResponse, HttpServletResponse servletResponse) {
        KakaoAccountResponse kakaoAccountResponse = requestUserInfoToKakao(kakaoTokenResponse.getAccess_token());
        return joinOrLoadUser(servletResponse, kakaoTokenResponse, kakaoAccountResponse);
    }

    // 회원가입 처리 -> 존재하면 정보 가져오기, 존재하지 않으면 새로 저장
    private LoginResponse joinOrLoadUser(HttpServletResponse servletResponse, KakaoTokenResponse kakaoTokenResponse, KakaoAccountResponse kakaoAccountResponse) {
        String kakaoEmail = kakaoAccountResponse.getKakao_account().getEmail();

        User existUser = userRepository.findByEmailIncludingDeleted(kakaoEmail)
                .orElse(null);

        UserAuthentication userAuthentication = userAuthenticationRepository.findByUserEmail(kakaoEmail)
                .orElse(UserAuthentication.of(kakaoTokenResponse, kakaoAccountResponse));

        // 첫 가입
        if (existUser == null) {
            log.info("첫가입");
            // 가입 처리
            HealthClub defaultHealthClub = findDefaultHealthClub();
            String tempNickname = generateTempNickname();

            User joinedUser = join(kakaoEmail, tempNickname, defaultHealthClub);

            userAuthentication.addUser(joinedUser);
            userAuthenticationRepository.save(userAuthentication);

            JwtUserClaimsDto claimsDto = JwtUserClaimsDto.defaultClaimsOf(kakaoEmail, tempNickname);
            addTokensInHeader(servletResponse, claimsDto);

            return newUserResponse(tempNickname);

        // 이전에 가입 후 탈퇴한 회원
        } else if (existUser.isDeleted()){
            log.info("가입했다 탈퇴한 회원 처리 실행");

            // todo : 가입했다 탈퇴한 회원 처리 -> 탈퇴해서 아이디가 남아있는 회원의 처리가 추가되어야 한다. 복구로직 또는 실패로직
            userAuthentication.updateTokens(kakaoTokenResponse.getAccess_token(), kakaoTokenResponse.getRefresh_token());
            userAuthenticationRepository.save(userAuthentication);

            return deletedUserResponse(existUser);

        // 이전에 이미 가입한 회원 -> 유저정보를 불러온다.
        } else {
            log.info("이전에 이미 가입한 회원 이전 accessToken={}",userAuthentication.getAccessToken());
            userAuthentication.updateTokens(kakaoTokenResponse.getAccess_token(), kakaoTokenResponse.getRefresh_token());
            userAuthentication.addUser(existUser);
            UserAuthentication afterSave = userAuthenticationRepository.save(userAuthentication);
            log.info("이전에 이미 가입한 회원 이후 accessToken={}",afterSave.getAccessToken());

            JwtUserClaimsDto claimsDto = JwtUserClaimsDto.from(existUser);
            addTokensInHeader(servletResponse, claimsDto);

            return existUserResponse(existUser);
        }
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
        return UUID.randomUUID().toString().replaceAll("-","_");
    }

    private User join(String kakaoEmail, String tempNickname, HealthClub defaultHealthClub) {
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

        return userRepository.save(user);
    }

    private void addTokensInHeader(HttpServletResponse response, JwtUserClaimsDto jwtUserClaimsDto) {
        String accessToken = Generator.generateAccessToken(jwtUserClaimsDto);
        String refreshToken = Generator.generateRefreshToken(jwtUserClaimsDto);

        response.addHeader(ACCESS_TOKEN.HTTP_HEADER_AUTHORIZATION, accessToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.SET_COOKIE, JwtUtils.createRefreshTokenCookie(refreshToken).toString());
        response.setCharacterEncoding(JwtConstants.CHARSET_UTF_8);
    }
}

