package com.ogjg.daitgym.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.ogjg.daitgym.alarm.dto.FcmTokenRequestDto;
import com.ogjg.daitgym.alarm.dto.NotificationRequestDto;
import com.ogjg.daitgym.alarm.repository.FcmTokenRepository;
import com.ogjg.daitgym.common.exception.exercise.NotFoundExercise;
import com.ogjg.daitgym.common.exception.fcmtoken.NotFoundFcmToken;
import com.ogjg.daitgym.common.exception.journal.NotFoundExerciseList;
import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.domain.FcmToken;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.exercise.Exercise;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseList;
import com.ogjg.daitgym.exercise.repository.ExerciseRepository;
import com.ogjg.daitgym.journal.repository.exerciselist.ExerciseListRepository;
import com.ogjg.daitgym.user.service.UserHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmAlarmService {

    private final UserHelper userHelper;
    private final ExerciseRepository exerciseRepository;
    private final FcmTokenRepository notificationRepository;
    private final ExerciseListRepository exerciseListRepository;
    private final FcmTokenRepository fcmTokenRepository;


    /**
     * FCM 토큰값 저장
     */
    @Transactional
    public void saveNotification(FcmTokenRequestDto fcmTokenRequestDto, OAuth2JwtUserDetails oAuth2JwtUserDetails) {
        User user = userHelper.findUserByEmail(oAuth2JwtUserDetails.getEmail());

        String token = fcmTokenRequestDto.getToken();

        if (!fcmTokenRepository.existsByUserAndToken(user, token)) {
            FcmToken notification = FcmToken.builder()
                    .token(token)
                    .user(user)
                    .build();
            notificationRepository.save(notification);
        } else {
            log.info("이미 토큰이 존재합니다.");
        }
    }


    public String alarmMessage(ExerciseJournal exerciseJournal) {
        String message = "";

        List<String> exercises = new ArrayList<>();
        StringBuilder messageBuilder = new StringBuilder("오늘은 ");

        List<ExerciseList> exerciseList = exerciseListRepository.findByExerciseJournalId(exerciseJournal.getId())
                .orElseThrow(NotFoundExerciseList::new);

        for (ExerciseList list : exerciseList) {
            Long exerciseId = list.getExercise().getId();
            Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(NotFoundExercise::new);
            exercises.add(exercise.getName());
        }

        for (int i = 0; i < exercises.size(); i++) {
            messageBuilder.append(exercises.get(i));
            if (i < exercises.size() - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append(" 하는 날입니다.");
        message = messageBuilder.toString();

        return message;
    }

    public void sendNotification(NotificationRequestDto requestDto) throws ExecutionException, InterruptedException {
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(WebpushNotification.builder()
                                .setTitle(requestDto.getTitle())
                                .setBody(requestDto.getMessage())
                                .build())
                        .build())
                .setToken(requestDto.getToken())
                .build();

        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        log.info("Send message : " + response);
    }


    public void deleteFcmToken(OAuth2JwtUserDetails oAuth2JwtUserDetails, FcmTokenRequestDto fcmTokenRequestDto) {
        User user = userHelper.findUserByEmail(oAuth2JwtUserDetails.getEmail());
        String token = fcmTokenRequestDto.getToken();

        FcmToken fcmToken = fcmTokenRepository.findByUserAndToken(user, token).orElseThrow(NotFoundFcmToken::new);
        fcmTokenRepository.delete(fcmToken);
    }

}
