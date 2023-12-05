package com.ogjg.daitgym.journal.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundExerciseJournal;
import com.ogjg.daitgym.common.exception.journal.*;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.exercise.Exercise;
import com.ogjg.daitgym.domain.journal.ExerciseHistory;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseJournalReplicationHistory;
import com.ogjg.daitgym.domain.journal.ExerciseList;
import com.ogjg.daitgym.exercise.service.ExerciseHelper;
import com.ogjg.daitgym.journal.dto.request.ExerciseListRequest;
import com.ogjg.daitgym.journal.dto.request.ReplicationRoutineDto;
import com.ogjg.daitgym.journal.dto.request.ReplicationRoutineRequestDto;
import com.ogjg.daitgym.journal.dto.response.dto.UserJournalDetailExerciseHistoryDto;
import com.ogjg.daitgym.journal.dto.response.dto.UserJournalDetailExerciseListDto;
import com.ogjg.daitgym.journal.repository.exercisehistory.ExerciseHistoryRepository;
import com.ogjg.daitgym.journal.repository.exerciselist.ExerciseListRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalReplicationHistoryRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
import com.ogjg.daitgym.routine.repository.RoutineRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import com.ogjg.daitgym.user.service.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExerciseJournalHelper {

    private final ExerciseJournalReplicationHistoryRepository exerciseJournalReplicationHistoryRepository;
    private final ExerciseJournalRepository exerciseJournalRepository;
    private final ExerciseListRepository exerciseListRepository;
    private final ExerciseHistoryRepository exerciseHistoryRepository;
    private final UserRepository userRepository;
    private final ExerciseHelper exerciseHelper;
    private final RoutineRepository routineRepository;
    private final UserHelper userHelper;

    /**
     * 일지 검색
     * 일지 Id로 일지 존재하는지 확인하기
     */
    public ExerciseJournal findExerciseJournalById(Long journalId) {
        return exerciseJournalRepository.findById(journalId)
                .orElseThrow(NotFoundJournal::new);
    }

    /**
     * 유저와 일지날자로
     * 일지조회
     */
    public ExerciseJournal findExerciseJournalByUserAndJournalDate(
            User user, LocalDate journalDate
    ) {
        return exerciseJournalRepository.findByJournalDateAndUser(journalDate, user)
                .orElseThrow(NotFoundExerciseJournal::new);
    }

    /**
     * 유저와 일지날자로
     * 일지조회존재 확인
     */
    public boolean existExerciseJournalByUserAndJournalDate(
            User user, LocalDate journalDate
    ) {
        return exerciseJournalRepository.findByJournalDateAndUser(journalDate, user)
                .isPresent();
    }

    /**
     * 일지 작성자인지 확인
     * 일지에 접근 권한이 있는지 확인
     */
    public ExerciseJournal isAuthorizedForJournal(String email, Long JournalId) {
        ExerciseJournal exerciseJournal = findExerciseJournalById(JournalId);

        if (!email.equals(exerciseJournal.getUser().getEmail())) {
            throw new UserNotAuthorizedForJournal();
        }

        return exerciseJournal;
    }

    /**
     * 일지 목록 검색
     * 일지 목록 ID로 일지목록 검색
     */
    public ExerciseList findExerciseListById(Long exerciseListId) {
        return exerciseListRepository.findById(exerciseListId)
                .orElseThrow(NotFoundExerciseList::new);
    }

    /**
     * 운동기록 Id로 운동기록 검색
     */
    public ExerciseHistory findExerciseHistoryById(Long exerciseHistoryId) {
        return exerciseHistoryRepository.findById(exerciseHistoryId)
                .orElseThrow(NotFoundExerciseHistory::new);
    }

    /**
     * 운동일지로 운동 목록들 찾기
     */
    public List<ExerciseList> findExerciseListsByJournal(ExerciseJournal exerciseJournal) {
        return exerciseListRepository.findByExerciseJournal(exerciseJournal);
    }

    /**
     * 운동목록으로 운동기록들 찾기
     */
    public List<ExerciseHistory> findExerciseHistoriesByExerciseList(ExerciseList exerciseList) {
        return exerciseHistoryRepository.findAllByExerciseList(exerciseList);
    }

    /**
     * 같은 날짜에 일지가 존재하는지 확인
     */
    public boolean checkForExistJournalSameDate(
            User user, LocalDate journalDate
    ) {
        return exerciseJournalRepository.findByJournalDateAndUser(journalDate, user)
                .isPresent();
    }

    /**
     * 운동일지의 운동기록들이 모두 완료된 상태인지 확인
     *
     * @param exerciseJournal 확인할 운동일지
     */
    public void checkAllExerciseHistoriesCompleted(
            ExerciseJournal exerciseJournal
    ) {
        List<ExerciseList> exerciseLists = findExerciseListsByJournal(exerciseJournal);
        exerciseLists.forEach(
                exerciseList -> {
                    findExerciseHistoriesByExerciseList(exerciseList).forEach(
                            exerciseHistory -> {
                                if (!exerciseHistory.isCompleted()) throw new NotCompletedExerciseHistory();
                            }
                    );
                }
        );
    }

    /**
     * 운동일지 공개여부 확인
     */
    public void checkExerciseJournalDisclosure(Long journalId) {
        if (!findExerciseJournalById(journalId).isVisible())
            throw new UserNotAuthorizedForJournal("공개된 운동일지가 아닙니다");
    }

    /**
     * 운동 목록들을 DTO로 변환
     */
    public List<UserJournalDetailExerciseListDto> exerciseListsChangeUserJournalDetailsDto(
            List<ExerciseList> journalList
    ) {
        return journalList.stream()
                .map(exerciseList -> new UserJournalDetailExerciseListDto(
                        exerciseList,
                        exerciseHelper.findExercisePartByExercise(exerciseList.getExercise()),
                        exerciseHistoriesChangeUserJournalDetailsDto(exerciseList)
                )).toList();
    }

    /**
     * 운동 기록을 DTO로 변환
     */
    public List<UserJournalDetailExerciseHistoryDto> exerciseHistoriesChangeUserJournalDetailsDto(
            ExerciseList exerciseList
    ) {
        return findExerciseHistoriesByExerciseList(exerciseList).stream()
                .map(UserJournalDetailExerciseHistoryDto::new)
                .toList();
    }

    public ExerciseJournal getReplicatedExerciseJournal(
            LocalDate journalDate, String email
    ) {
        if (existExerciseJournalByUserAndJournalDate(userHelper.findUserByEmail(email), journalDate)) {
            return findExerciseJournalByUserAndJournalDate(userHelper.findUserByEmail(email), journalDate);
        }
        return createJournal(email, journalDate);
    }

    /**
     * 빈 운동일지 생성하기
     * 해당 날짜에 생성된 일지가 있으면 예외 발생
     */
    public ExerciseJournal createJournal(String email, LocalDate journalDate) {
        User user = userHelper.findUserByEmail(email);

        if (checkForExistJournalSameDate(user, journalDate)) {
            throw new AlreadyExistExerciseJournal();
        }

        return exerciseJournalRepository.save(
                ExerciseJournal.createJournal(user, journalDate)
        );
    }

    /**
     * 다른 사람의 운동목록과 하위 운동기록들
     * 내 일지로 복사해서 가져오기
     *
     * @param replicatedUserJournal 복사된 유저의 운동일지
     * @param originalExerciseLists 복사할 원본 운동일지
     */
    public void replicateExerciseListAndHistoryByJournal(
            ExerciseJournal replicatedUserJournal,
            List<ExerciseList> originalExerciseLists
    ) {
        List<ExerciseHistory> replicatedExerciseHistories = new ArrayList<>();

        originalExerciseLists.forEach(originalExerciseList -> {
            ExerciseList replicatedExerciseList = replicateExerciseListByJournal(replicatedUserJournal, originalExerciseList);
            replicateExerciseHistoriesByJournal(originalExerciseList, replicatedExerciseHistories, replicatedExerciseList);
        });

        exerciseHistoryRepository.saveAll(replicatedExerciseHistories);
    }

    /**
     * 다른 사람 운동목록의 운동기록들 복사해서 저장할 객체에 담기
     *
     * @param originalExerciseList        복사할 운동 기록들을 가지고 있는 원본 운동목록
     * @param replicatedExerciseHistories 복사된 운동기록들을 담을 객체
     * @param replicatedExerciseList      복사된 운동기록들을 담을 복사된 운동목록
     */
    private void replicateExerciseHistoriesByJournal(
            ExerciseList originalExerciseList,
            List<ExerciseHistory> replicatedExerciseHistories,
            ExerciseList replicatedExerciseList
    ) {
        findExerciseHistoriesByExerciseList(originalExerciseList).forEach(exerciseHistory ->
                replicatedExerciseHistories.add(
                        ExerciseHistory.replicateExerciseHistoryByJournal(replicatedExerciseList, exerciseHistory)
                )
        );
    }

    /**
     * 다른 사람 운동일지의 운동목록
     * 내 일지에 복사해서 가져오기
     *
     * @param replicatedUserJournal 복사하는 사람의 새로 생성된 운동일지
     * @param originalJournalList   복사하는 운동일지의 운동목록 원본 데이터
     * @return 복사된 운동목록
     */
    private ExerciseList replicateExerciseListByJournal(
            ExerciseJournal replicatedUserJournal,
            ExerciseList originalJournalList
    ) {
        return exerciseListRepository.save(
                ExerciseList.replicateExerciseListByJournal(replicatedUserJournal, originalJournalList)
        );
    }

    /**
     * 루틴의 운동목록과 하위 운동기록들
     * 내 일지로 복사해서 가져오기
     *
     * @param replicatedUserJournal        복사된 유저의 운동일지
     * @param replicationRoutineRequestDto 복사할 원본 운동일지
     */
    public void replicateExerciseListAndHistoryByRoutine(
            ExerciseJournal replicatedUserJournal,
            ReplicationRoutineRequestDto replicationRoutineRequestDto
    ) {
        List<ReplicationRoutineDto> originalExerciseLists = routineRepository.getOriginalRoutinesToReplicateExerciseLists(replicationRoutineRequestDto.getDayId());

        originalExerciseLists.forEach(
                replicationRoutineExerciseList -> {
                    ExerciseList replicatedExerciseList = replicateExerciseListByRoutine(replicatedUserJournal, replicationRoutineExerciseList);
                    replicateExerciseHistoriesByRoutine(replicationRoutineRequestDto.getDayId(), replicationRoutineExerciseList.getExerciseId(), replicatedExerciseList);
                }
        );
    }

    /**
     * 루틴에서 운동일지로 운동목록 가져오기
     *
     * @param replicatedUserJournal 복사해서 가져온 운동일지
     * @param replicationRoutine    원본 루틴 정보
     */
    private ExerciseList replicateExerciseListByRoutine(
            ExerciseJournal replicatedUserJournal,
            ReplicationRoutineDto replicationRoutine
    ) {
        return exerciseListRepository.save(
                ExerciseList.replicateExerciseListByRoutine(
                        replicatedUserJournal, replicationRoutine,
                        exerciseHelper.findExercise(replicationRoutine.getExerciseId())
                )
        );
    }

    /**
     * 루틴에서 운동기록들 운동일지로 가져오기
     *
     * @param originalDayId                    원본 루틴의 dayId
     * @param replicatedExerciseListExerciseId 복사된 일지의 운동목록의 운동Id
     * @param replicatedExerciseList           복사된 운동목록
     */
    private void replicateExerciseHistoriesByRoutine(
            Long originalDayId, Long replicatedExerciseListExerciseId,
            ExerciseList replicatedExerciseList
    ) {
        List<ReplicationRoutineDto> originalExerciseHistories =
                routineRepository.getOriginalRoutinesToReplicateExerciseHistories(originalDayId, replicatedExerciseListExerciseId);

        List<ExerciseHistory> replicatedExerciseHistories = originalExerciseHistories.stream()
                .map(replicationRoutineHistory ->
                        ExerciseHistory.replicateExerciseHistoryByRoutine(
                                replicatedExerciseList, replicationRoutineHistory)
                ).toList();

        exerciseHistoryRepository.saveAll(replicatedExerciseHistories);
    }

    /**
     * 운동목록 생성하기
     */
    public ExerciseList saveExerciseList(
            ExerciseJournal userJournal,
            Exercise exercise,
            ExerciseListRequest exerciseListRequest
    ) {
        return exerciseListRepository.save(
                ExerciseList.createExerciseList(userJournal, exercise, exerciseListRequest)
        );
    }

    /**
     * 일지 가져오기시 가져온 기록 저장
     */
    public void saveReplicationHistory(
            String userEmail, ExerciseJournal originalJournal, ExerciseJournal replicatedUserJournal
    ) {
        exerciseJournalReplicationHistoryRepository.save(
                new ExerciseJournalReplicationHistory(
                        userHelper.findUserByEmail(userEmail),
                        originalJournal,
                        replicatedUserJournal
                )
        );
    }
}
