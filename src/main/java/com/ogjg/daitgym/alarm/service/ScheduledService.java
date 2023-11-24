package com.ogjg.daitgym.alarm.service;

import com.ogjg.daitgym.alarm.dto.NotificationRequestDto;
import com.ogjg.daitgym.alarm.repository.FcmTokenRepository;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundExerciseJournal;
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
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final FcmAlarmService notificationService;
    private final FcmTokenRepository fcmTokenRepository;
    private final ExerciseJournalRepository exerciseJournalRepository;


    /**
     * 오전 8시에 알림 메세지 보내기
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void scheduledSend() throws ExecutionException, InterruptedException {

        List<FcmToken> fcmTokens = fcmTokenRepository.findAll();

        if (fcmTokens.isEmpty()) {
            log.info("fcmToken이 없어, 작업을 하지 않습니다.");
            return;
        }

        for (FcmToken fcmToken : fcmTokens) {
            User user = fcmToken.getUser();
            ExerciseJournal exerciseJournal = exerciseJournalRepository.findByUserAndJournalDate(user, LocalDate.now()).orElseThrow(NotFoundExerciseJournal::new);

            String message = notificationService.alarmMessage(exerciseJournal);

            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .title("[DaItGym 운동 알림]")
                    .message(message)
                    .token(fcmToken.getToken())
                    .build();

            notificationService.sendNotification(notificationRequestDto);
        }
        log.info("웹푸시 보냈어!");
    }
}
