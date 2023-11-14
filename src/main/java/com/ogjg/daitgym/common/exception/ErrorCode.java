package com.ogjg.daitgym.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements ErrorType {

    SUCCESS(HttpStatus.OK, "200", "OK"),
    ALREADY_FOLLOW_USER(HttpStatus.NOT_FOUND, "404", "이미 팔로우한 유저입니다"),
    RANGE_OVER_IMAGES(HttpStatus.BAD_REQUEST,"400","이미지의 개수는 10개를 초과할 수 없습니다"),
    NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "404", "팔로우를 먼저 해주세요"),
    NOT_FOUND_EXERCISE(HttpStatus.NOT_FOUND, "404", "운동을 찾을 수 없습니다"),
    NOT_FOUND_FEED_JOURNAL_IMAGE(HttpStatus.NOT_FOUND, "404", "운동일지 피드의 이미지를 찾을 수 없습니다"),
    NOT_FOUND_EXERCISE_PART(HttpStatus.NOT_FOUND, "404", "운동부위를 찾을 수 없습니다"),
    NOT_FOUND_EXERCISE_LIST(HttpStatus.NOT_FOUND, "404", "운동 목록을 찾을 수 없습니다"),
    NOT_FOUND_EXERCISE_HISTORY(HttpStatus.NOT_FOUND, "404", "운동 기록을 찾을 수 없습니다"),
    NOT_FOUND_JOURNAL(HttpStatus.NOT_FOUND, "404", "운동일지를 찾을 수 없습니다"),
    USER_NOT_AUTHORIZED_JOURNAL(HttpStatus.FORBIDDEN, "403", "운동일지에 접근 권한이 없습니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "404", "유저를 찾을 수 없습니다"),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "404", "데이터 검증 실패"),
    NOT_FOUND_FEED_JOURNAL(HttpStatus.BAD_REQUEST, "404", "운동일지 피드를 찾을 수 없습니다"),
    NOT_FOUND_FEED_JOURNAL_COMMENT(HttpStatus.BAD_REQUEST, "404", "운동일지 댓글을 찾을 수 없습니다"),
    NOT_FOUND_HEALTH_CLUB(HttpStatus.NOT_FOUND, "404", "헬스장을 찾을 수 없습니다"),
    NOT_FOUND_SPLIT_TITLE(HttpStatus.NOT_FOUND, "404", "분할 명칭을 찾을 수 없습니다."),
    WRONG_APPROACH(HttpStatus.FORBIDDEN, "403", "잘못된 접근입니다"),
    NOT_FOUND_ROUTINE(HttpStatus.BAD_REQUEST, "404", "루틴을 찾을 수 없습니다"),
    NOT_FOUND_ROUTINE_COMMENT(HttpStatus.BAD_REQUEST, "404", "루틴 댓글을 찾을 수 없습니다"),
    NOT_FOUND_EXERCISE_JOURNAL(HttpStatus.BAD_REQUEST, "404", "운동일지를 찾을 수 없습니다"),
    NOT_FOUND_CHATTING_ROOM(HttpStatus.BAD_REQUEST, "404", "채팅방을 찾을 수 없습니다"),
    REFRESH_TOKEN_AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "401", "Refresh Token 인증 오류"),
    ACCESS_TOKEN_AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "401", "Access Token 인증 오류"),
    NOT_FOUND_SCRAPED_USER_ROUTINE(HttpStatus.NOT_FOUND, "404", "스크랩 된 유저의 루틴을 찾을 수 없습니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "400", "중복되는 닉네임입니다."),
    UNAUTHORIZED_USER_ACCESS(HttpStatus.FORBIDDEN, "403", "접근 권한이 부족합니다."),
    NO_EXERCISE_IN_ROUTINE(HttpStatus.NOT_FOUND, "404", "루틴의 운동을 찾을 수 없습니다."),
    EMPTY_TRAINER_APPLY_APPROVAL(HttpStatus.BAD_REQUEST, "400", "잘못된 승인 신청입니다. 자격 혹은 수상내역 입력이 누락되었습니다."),
    ALREADY_SCRAPED_ROUTINE(HttpStatus.BAD_REQUEST, "400", "이미 스크랩한 루틴입니다.");

    @JsonIgnore
    private final HttpStatus statusCode;
    private final String code;
    private String message;

    ErrorCode(
            HttpStatus statusCode,
            String code,
            String message
    ) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
    }

    public ErrorResult changeMessage(
            String message
    ) {
        return new ErrorResult(this.statusCode, this.getCode(), message);
    }
}
