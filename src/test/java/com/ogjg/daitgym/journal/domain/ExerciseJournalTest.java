package com.ogjg.daitgym.journal.domain;

import com.ogjg.daitgym.domain.Role;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseJournalTest {

    private User writer;
    private ExerciseJournal visibleJournal;
    private ExerciseJournal invisibleJournal;

    @BeforeEach
    public void setUp() {
        writer = User.builder()
                .email("user@google.com")
                .nickname("user1")
                .phoneNumber("010-1111-1111")
                .introduction("hello")
                .role(Role.USER)
                .isDeleted(false)
                .build();

        visibleJournal = ExerciseJournal.builder()
                .user(writer)
                .isVisible(true)
                .build();

        invisibleJournal = ExerciseJournal.builder()
                .user(writer)
                .isVisible(false)
                .build();
    }

    @DisplayName("공유된 운동 일지 조회 가능 여부 확인 - 공개 운동일지에 대해 접근한 사람의 이메일이 같으면")
    @Test
    public void isAccessibleByWriterWhenIsVisible() {
        //given
        String writerEmail = writer.getEmail();

        // when
        boolean accessible = visibleJournal.isAccessibleBy(writerEmail);

        // then
        assertThat(accessible).isTrue();
    }

    @DisplayName("공유된 운동 일지 조회 가능 여부 확인 - 공개 운동일지에 대해 접근한 사람의 이메일이 다르더라도 접근 가능")
    @Test
    public void isAccessibleByAnotherUserWhenIsVisible() {
        // given
        String anotherUserEmail = "anotherUser@google.com";

        // when
        boolean accessible = visibleJournal.isAccessibleBy(anotherUserEmail);

        // then
        assertThat(accessible).isTrue();
    }

    @DisplayName("공유된 운동 일지 조회 가능 여부 확인 - 비공개 운동일지일때, 접근한 사람의 이메일이 같으면 작성자이므로 접근 가능")
    @Test
    public void isAccessibleByWriterWhenIsNotVisible() {
        // given
        String writerEmail = writer.getEmail();

        // when
        boolean accessible = invisibleJournal.isAccessibleBy(writerEmail);

        // then
        assertThat(accessible).isTrue();
     }

    @DisplayName("공유된 운동 일지 조회 가능 여부 확인 - 비공개 운동일지일때, 접근한 사람의 이메일이 다르면 접근 불가")
    @Test
    public void isNotAccessibleByAnotherUserWhenIsNotVisible() {
        // given
        String anotherUserEmail = "anotherUser@google.com";

        // when
        boolean accessible = invisibleJournal.isAccessibleBy(anotherUserEmail);

        // then
        assertThat(accessible).isFalse();
    }
}
