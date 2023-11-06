package com.ogjg.daitgym.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.config.security.jwt.dto.JwtUserClaimsDto;
import com.ogjg.daitgym.config.security.jwt.util.JwtUtils;
import com.ogjg.daitgym.domain.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ogjg.daitgym.config.security.jwt.util.JwtUtils.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    /**
     * [path]
     * /api/token/new
     * /api/token/test
     *
     * [json body 예시]
         {
            "email" : "hi@gmail.com",
            "nickname" : "ljj",
            "role" : "ROLE_USER" // Role의 key 중 필요한 값으로 입력
         }
     */

    /**
     * 토큰 임시 발급 로직
     */
    @PostMapping("/new")
    public ApiResponse<Void> createToken(
            @RequestBody TokenTestRequest tokenTestRequest,
            HttpServletResponse response
    ) {
        JwtUserClaimsDto claimsDto = JwtUserClaimsDto.builder()
                .email(tokenTestRequest.email)
                .role(Role.fromKey(tokenTestRequest.role))
                .build();

        String accessToken = JwtUtils.TokenGenerator.generateAccessToken(claimsDto);

        addTokenInHeader(response, accessToken);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 토큰 동작 테스트
     */
    @GetMapping("/test")
    public String testToken(
            @AuthenticationPrincipal OAuth2JwtUserDetails userDetails
    ) throws JsonProcessingException {

        String responseData = String.format(
                "이메일 : %s\n" +
                "닉네임 : %s\n" +
                "권한 : %s",
                userDetails.getEmail(),
                userDetails.getNickname(),
                userDetails.findAnyFirstRole());

        return responseData;
    }

    private void addTokenInHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + accessToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHARSET_UTF_8);
    }

    static class TokenTestRequest {
        private String email;
        private String nickname;

        private String role;
        public TokenTestRequest(String email, String nickname, String role) {
            this.email = email;
            this.nickname = nickname;
            this.role = role;
        }

    }

}
