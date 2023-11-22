package com.ogjg.daitgym.user.constants;

import com.ogjg.daitgym.domain.ExerciseSplit;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserConstants {
    public static final String DEFAULT_HEALTH_CLUB_NAME = "";
    @Value("${cloud.aws.default.profile-img}")
    public String awsDefaultUrlTemp;
    public static String AWS_DEFAULT_PROFILE_IMG_URL;
    public static final String DEFAULT_INTRODUCTION = DEFAULT_HEALTH_CLUB_NAME;
    public static final String DEFAULT_PREFERRED_SPLIT = ExerciseSplit.ONE_DAY.getTitle();

    public static final boolean ALREADY_JOINED = true;
    public static final boolean NOT_ALREADY_JOINED = false;
    public static final boolean DELETED = true;
    public static final boolean NOT_DELETED = false;

    @PostConstruct
    public void setUrl() {
        AWS_DEFAULT_PROFILE_IMG_URL = awsDefaultUrlTemp;
    }
}
