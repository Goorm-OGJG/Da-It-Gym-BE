package com.ogjg.daitgym.journal.service;

import com.ogjg.daitgym.domain.Exercise;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.journal.ExerciseHistory;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseList;
import com.ogjg.daitgym.journal.dto.request.ExerciseHistoryRequest;
import com.ogjg.daitgym.journal.dto.request.ExerciseListRequest;
import com.ogjg.daitgym.journal.exception.NotFoundExercise;
import com.ogjg.daitgym.journal.exception.NotFoundExerciseList;
import com.ogjg.daitgym.journal.exception.NotFoundJournal;
import com.ogjg.daitgym.journal.exception.UserNotAuthorizedForJournal;
import com.ogjg.daitgym.journal.repository.exercise.ExerciseRepository;
import com.ogjg.daitgym.journal.repository.exercisehistory.ExerciseHistoryRepository;
import com.ogjg.daitgym.journal.repository.exerciselist.ExerciseListRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseJournalService {

    private final ExerciseJournalRepository exerciseJournalRepository;
    private final ExerciseListRepository exerciseListRepository;
    private final ExerciseHistoryRepository exerciseHistoryRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    /**
     * 빈 운동일지 생성하기
     */
    @Transactional
    public void createJournal(String email, LocalDate journalDate) {
        User user = userRepository.findById(email).orElseThrow(NotFoundUser::new);

        exerciseJournalRepository.save(
                ExerciseJournal.createJournal(user, journalDate)
        );
    }

    /**
     * 운동일지에 운동 추가하기
     */
    @Transactional
    public void createExerciseList(
            String email,
            ExerciseListRequest exerciseListRequest
    ) {

        ExerciseJournal userJournal = isAuthorizedForJournal(email, exerciseListRequest.getExerciseJournalId());
        Exercise userExercise = findExercise(exerciseListRequest.getExerciseName());

        ExerciseList exerciseList = exerciseListRepository.save(
                ExerciseList.createExercise(
                        userJournal,
                        userExercise,
                        exerciseListRequest
                )
        );

        List<ExerciseHistoryRequest> defaultExerciseHistory =
                exerciseListRequest.getExerciseSets()
                        .stream()
                        .map(exerciseHistoryRequest -> exerciseHistoryRequest.putExerciseListId(exerciseList.getId()))
                        .toList();

        defaultExerciseHistory.forEach(
                exerciseHistoryRequest -> createExerciseHistory(email, exerciseHistoryRequest)
        );
    }

    /**
     * 운동목록에 운동 기록 생성하기
     */
    @Transactional
    public void createExerciseHistory(String email, ExerciseHistoryRequest exerciseHistoryRequest) {

        ExerciseList exerciseList = findExerciseList(exerciseHistoryRequest.getExerciseListId());
        isAuthorizedForJournal(email, exerciseList.getExerciseJournal().getId());

        exerciseHistoryRepository.save(
                ExerciseHistory.createExerciseHistory(exerciseList, exerciseHistoryRequest)
        );
    }

    /**
     * 일지 작성자인지 확인
     */
    private ExerciseJournal isAuthorizedForJournal(String email, Long JournalId) {
        ExerciseJournal exerciseJournal = findExerciseJournal(JournalId);

        if (!email.equals(exerciseJournal.getUser().getEmail())) {
            throw new UserNotAuthorizedForJournal();
        }

        return exerciseJournal;
    }

    /**
     * 일지 검색
     * 일지 Id로 일지 존재하는지 확인하기
     */
    private ExerciseJournal findExerciseJournal(Long journalId) {
        return exerciseJournalRepository.findById(journalId)
                .orElseThrow(NotFoundJournal::new);
    }

    /**
     * 운동검색
     * 운동이름으로 운동 존재하는지 확인하기
     */
    private Exercise findExercise(String exerciseName) {
        return exerciseRepository.findByName(exerciseName)
                .orElseThrow(NotFoundExercise::new);
    }

    /**
     * 일지 목록 검색
     * 일지 목록 ID로 일지목록 검색
     */
    private ExerciseList findExerciseList(Long exerciseListId) {
        return exerciseListRepository.findById(exerciseListId)
                .orElseThrow(NotFoundExerciseList::new);
    }

}
