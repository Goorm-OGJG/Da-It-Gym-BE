package com.ogjg.daitgym.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.ogjg.daitgym.alarm.dto.NotificationRequestDto;
import com.ogjg.daitgym.common.exception.exercise.NotFoundExercise;
import com.ogjg.daitgym.common.exception.journal.NotFoundExerciseList;
import com.ogjg.daitgym.domain.exercise.Exercise;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseList;
import com.ogjg.daitgym.exercise.repository.ExerciseRepository;
import com.ogjg.daitgym.journal.repository.exerciselist.ExerciseListRepository;
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

    private final ExerciseRepository exerciseRepository;
    private final ExerciseListRepository exerciseListRepository;


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
}
