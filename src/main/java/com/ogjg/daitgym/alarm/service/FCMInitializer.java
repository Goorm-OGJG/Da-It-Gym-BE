package com.ogjg.daitgym.alarm.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class FCMInitializer {

    @Value("${firebase.sdk.path}")
    private String FIREBASE_CONFIG_PATH;

    /**
     * ClassPathResource()는 resources 폴더 아래에 있는 괄호 안 경로를 찾는다.
     * 우리가 이동시킨 json파일을 찾아서 맞는 정보인지 확인한 후 FirebaseApp.initializeApp()을 통해서 실행한다.
     */
    @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.info("FCM error");
            log.error("FCM error message : " + e.getMessage());
        }
    }
}