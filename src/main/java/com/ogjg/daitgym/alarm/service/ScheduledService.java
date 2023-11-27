package com.ogjg.daitgym.alarm.service;

import com.ogjg.daitgym.alarm.dto.NotificationRequestDto;
import com.ogjg.daitgym.alarm.repository.FcmTokenRepository;
import com.ogjg.daitgym.domain.FcmToken;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final FcmAlarmService notificationService;
    private final FcmTokenRepository fcmTokenRepository;
    private final ExerciseJournalRepository exerciseJournalRepository;


    /**
     * 8시에 알림 메세지 보내기
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduledSend() throws ExecutionException, InterruptedException {

        List<FcmToken> fcmTokens = fcmTokenRepository.findAll();

        if (fcmTokens.isEmpty()) {
            log.info("FCM 토큰이 비어있어, 알림전송 실패");
            return;
        }

        for (FcmToken fcmToken : fcmTokens) {
            User user = fcmToken.getUser();
            Optional<ExerciseJournal> optionalExerciseJournal = exerciseJournalRepository.findByUserAndJournalDate(user, LocalDate.now());

            if (optionalExerciseJournal.isPresent()) {
                ExerciseJournal exerciseJournal = optionalExerciseJournal.get();
                String message = notificationService.alarmMessage(exerciseJournal);

                NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                        .title("[DaItGym 운동 알림]")
                        .message(message)
                        .token(fcmToken.getToken())
                        .build();

                notificationService.sendNotification(notificationRequestDto);

            } else {
                NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                        .title("[DaItGym 운동 알림]")
                        .message("오늘 운동일지가 비어있어요!")
                        .token(fcmToken.getToken())
                        .build();

                notificationService.sendNotification(notificationRequestDto);
            }
        }
        log.info("웹푸시 보냈어!");
    }
}
