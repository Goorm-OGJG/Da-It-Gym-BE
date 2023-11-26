package com.ogjg.daitgym.alarm.service;

import com.ogjg.daitgym.alarm.dto.FcmTokenRequestDto;
import com.ogjg.daitgym.alarm.repository.FcmTokenRepository;
import com.ogjg.daitgym.common.exception.fcmtoken.NotFoundFcmToken;
import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.domain.FcmToken;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.user.service.UserHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final UserHelper userHelper;
    private final FcmTokenRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;


    public boolean getNotification(OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        return fcmTokenRepository.findByUserEmail(oAuth2JwtUserDetails.getEmail()).isPresent();
    }

    @Transactional
    public void updateNotification(FcmTokenRequestDto fcmTokenRequestDto, OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        User user = userHelper.findUserByEmail(oAuth2JwtUserDetails.getEmail());
        String token = fcmTokenRequestDto.getToken();

        FcmToken fcmToken = fcmTokenRepository.findByUser(user).orElseThrow(NotFoundUser::new);
        fcmToken.update(token);
        fcmTokenRepository.save(fcmToken);
    }

    @Transactional
    public void saveNotification(FcmTokenRequestDto fcmTokenRequestDto, OAuth2JwtUserDetails oAuth2JwtUserDetails) {

        try {
            User user = userHelper.findUserByEmail(oAuth2JwtUserDetails.getEmail());
            String token = fcmTokenRequestDto.getToken();

            if (!fcmTokenRepository.existsByUserAndToken(user, token)) {
                FcmToken notification = FcmToken.builder()
                        .token(token)
                        .user(user)
                        .build();
                notificationRepository.save(notification);
                log.info("토큰 저장 성공");
            } else {
                log.info("이미 토큰이 존재합니다.");
            }
        } catch (Exception e) {
            log.error("트랜잭션 롤백 발생", e);
            throw e;
        }
    }

    public void deleteFcmToken(FcmTokenRequestDto fcmTokenRequestDto, OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        User user = userHelper.findUserByEmail(oAuth2JwtUserDetails.getEmail());
        String token = fcmTokenRequestDto.getToken();

        FcmToken fcmToken = fcmTokenRepository.findByUserAndToken(user, token).orElseThrow(NotFoundFcmToken::new);
        fcmTokenRepository.delete(fcmToken);
    }
}
