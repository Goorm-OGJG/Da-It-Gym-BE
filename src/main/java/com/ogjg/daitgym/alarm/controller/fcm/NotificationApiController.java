package com.ogjg.daitgym.alarm.controller.fcm;

import com.ogjg.daitgym.alarm.dto.FcmTokenRequestDto;
import com.ogjg.daitgym.alarm.service.FcmAlarmService;
import com.ogjg.daitgym.common.exception.ErrorCode;
import com.ogjg.daitgym.common.response.ApiResponse;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationApiController {

    private final FcmAlarmService notificationService;

    @PostMapping("/token")
    public ApiResponse<Void> saveNotification(@RequestBody FcmTokenRequestDto fcmTokenRequestDto,
                                              @AuthenticationPrincipal OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        notificationService.saveNotification(fcmTokenRequestDto, oAuth2JwtUserDetails);
        return new ApiResponse<>(ErrorCode.SUCCESS);
    }
}
