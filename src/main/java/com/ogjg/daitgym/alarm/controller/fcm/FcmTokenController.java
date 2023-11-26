package com.ogjg.daitgym.alarm.controller.fcm;

import com.ogjg.daitgym.alarm.dto.FcmTokenRequestDto;
import com.ogjg.daitgym.alarm.service.FcmTokenService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;


    @GetMapping("/token")
    public ApiResponse<Boolean> getNotification(@AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        return new ApiResponse<>(ErrorCode.SUCCESS, fcmTokenService.getNotification(oAuth2JwtUserDetails));
    }

    @PostMapping("/token")
    public ApiResponse<Void> saveNotification(@RequestBody FcmTokenRequestDto fcmTokenRequestDto,
                                              @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        fcmTokenService.saveNotification(fcmTokenRequestDto, oAuth2JwtUserDetails);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }

    @PatchMapping("/token")
    public ApiResponse<Void> updateNotification(@RequestBody FcmTokenRequestDto fcmTokenRequestDto,
                                                @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        fcmTokenService.updateNotification(fcmTokenRequestDto, oAuth2JwtUserDetails);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }
}
