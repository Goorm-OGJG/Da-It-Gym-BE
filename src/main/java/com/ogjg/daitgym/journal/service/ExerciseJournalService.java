package com.ogjg.daitgym.journal.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundExerciseJournal;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundUser;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.exercise.Exercise;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseHistory;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseList;
import com.ogjg.daitgym.exercise.service.ExerciseService;
import com.ogjg.daitgym.feed.service.FeedExerciseJournalService;
import com.ogjg.daitgym.journal.dto.request.*;
import com.ogjg.daitgym.journal.dto.response.UserJournalDetailResponse;
import com.ogjg.daitgym.journal.dto.response.UserJournalListResponse;
import com.ogjg.daitgym.journal.dto.response.dto.UserJournalDetailDto;
import com.ogjg.daitgym.journal.dto.response.dto.UserJournalDetailExerciseHistoryDto;
import com.ogjg.daitgym.journal.dto.response.dto.UserJournalDetailExerciseListDto;
import com.ogjg.daitgym.journal.dto.response.dto.UserJournalListDto;
import com.ogjg.daitgym.journal.exception.NotFoundExerciseHistory;
import com.ogjg.daitgym.journal.exception.NotFoundExerciseList;
import com.ogjg.daitgym.journal.exception.NotFoundJournal;
import com.ogjg.daitgym.journal.exception.UserNotAuthorizedForJournal;
import com.ogjg.daitgym.journal.repository.exercisehistory.ExerciseHistoryRepository;
import com.ogjg.daitgym.journal.repository.exerciselist.ExerciseListRepository;
import com.ogjg.daitgym.journal.repository.journal.ExerciseJournalRepository;
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
    private final UserRepository userRepository;
    private final ExerciseService exerciseService;
    private final FeedExerciseJournalService feedExerciseJournalService;

    /**
     * 빈 운동일지 생성하기
     */
    @Transactional
    public void createJournal(String email, LocalDate journalDate) {
        exerciseJournalRepository.save(
                ExerciseJournal.createJournal(findUserByEmail(email), journalDate)
        );
    }

    /**
     * 운동일지 완료하기
     */
    @Transactional
    public void exerciseJournalComplete(
            Long journalId, String email,
            ExerciseJournalCompleteRequest exerciseJournalCompleteRequest
    ) {
        isAuthorizedForJournal(email, journalId);
        ExerciseJournal exerciseJournal = findExerciseJournal(journalId);
        exerciseJournal.journalComplete(exerciseJournalCompleteRequest);
    }

    /**
     * 운동일지 공개 여부
     * 공개시 피드에 추가
     */
    @Transactional
    public void exerciseJournalShare(
            Long journalId, String email,
            ExerciseJournalShareRequest exerciseJournalShareRequest
    ) {
        isAuthorizedForJournal(email, journalId);
        ExerciseJournal exerciseJournal = findExerciseJournal(journalId);
        exerciseJournal.journalShareToFeed(exerciseJournalShareRequest);

        if (exerciseJournal.isVisible()) {
            feedExerciseJournalService.shareJournalFeed(
                    exerciseJournal, exerciseJournalShareRequest.getImgFiles()
            );
        }
    }

    /**
     * 운동 목록 휴식시간 변경
     */
    @Transactional
    public void changeExerciseListRestTime(
            String email, Long exerciseListId,
            UpdateRestTimeRequest updateRestTimeRequest
    ) {
        ExerciseList exerciseList = findExerciseList(exerciseListId);
        isAuthorizedForJournal(email, exerciseList.getExerciseJournal().getId());
        exerciseList.changeRestTime(updateRestTimeRequest);
    }

    /**
     * 운동일지 삭제시
     * 피드 운동일지, 피드 좋아요, 피드 댓글 삭제
     * 운동기록, 운동목록, 운동일지 삭제하기
     */
    @Transactional
    public void deleteJournal(String email, Long journalId) {
        ExerciseJournal journal = isAuthorizedForJournal(email, journalId);

        FeedExerciseJournal feedJournal = feedExerciseJournalService.findFeedJournalByJournal(journal);
        feedExerciseJournalService.deleteFeedJournal(email, feedJournal.getId());

        List<ExerciseList> exerciseLists = findExerciseListByJournal(journal);
        exerciseLists.forEach(exerciseHistoryRepository::deleteAllByExerciseList);
        exerciseListRepository.deleteAllByExerciseJournal(journal);

        exerciseJournalRepository.delete(journal);
    }

    /**
     * 운동목록 삭제하기
     * 운동기록 삭제
     */
    @Transactional
    public void deleteExerciseList(String email, Long exerciseListId) {
        ExerciseList exerciseList = findExerciseList(exerciseListId);

        isAuthorizedForJournal(email, exerciseList.getExerciseJournal().getId());

        exerciseHistoryRepository.deleteAllByExerciseList(exerciseList);
        exerciseListRepository.delete(exerciseList);
    }

    /**
     * 운동기록 삭제하기
     */
    @Transactional
    public void deleteExerciseHistory(String email, Long exerciseHistoryId) {
        ExerciseHistory exerciseHistory = findExerciseHistoryById(exerciseHistoryId);

        isAuthorizedForJournal(
                email,
                exerciseHistory.getExerciseList()
                        .getExerciseJournal()
                        .getId()
        );

        exerciseHistoryRepository.delete(exerciseHistory);
    }

    /**
     * 운동기록 변경하기
     */
    @Transactional
    public void updateExerciseHistory(
            String email, Long exerciseHistoryId,
            UpdateExerciseHistoryRequest updateExerciseHistoryRequest
    ) {
        ExerciseHistory exerciseHistory = findExerciseHistoryById(exerciseHistoryId);

        isAuthorizedForJournal(email, exerciseHistory.getExerciseList().getExerciseJournal().getId());

        exerciseHistory.updateHistory(updateExerciseHistoryRequest);
    }

    /**
     * 운동일지에 운동 추가하기
     */
    @Transactional
    public void createExerciseList(
            String email,
            ExerciseListRequest exerciseListRequest
    ) {

        ExerciseJournal userJournal = isAuthorizedForJournal(email, exerciseListRequest.getId());
        Exercise userExercise = exerciseService.findExercise(exerciseListRequest.getName());

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

        ExerciseList exerciseList = findExerciseList(exerciseHistoryRequest.getId());
        isAuthorizedForJournal(email, exerciseList.getExerciseJournal().getId());

        exerciseHistoryRepository.save(
                ExerciseHistory.createExerciseHistory(exerciseList, exerciseHistoryRequest)
        );
    }

    /**
     * 내 운동일지 목록
     */
    @Transactional(readOnly = true)
    public UserJournalListResponse userJournalList(
            String email
    ) {
        User user = findUserByEmail(email);
        List<UserJournalListDto> userJournalListDtoList = exerciseJournalRepository.findAllByUser(user)
                .stream()
                .map(UserJournalListDto::new)
                .toList();

        return new UserJournalListResponse(userJournalListDtoList);
    }

    /**
     * 내 운동일지 상세보기
     * todo 개별조회로 인한 성능이슈 발생 가능성이 보임 추후 Join을 통해 한번에 가져오도록 개선필요로 보임
     */
    @Transactional(readOnly = true)
    public UserJournalDetailResponse userJournalDetail(
            LocalDate journalDate, String email
    ) {
        ExerciseJournal exerciseJournal = findExerciseJournalByUserAndJournalDate(findUserByEmail(email), journalDate);
        isAuthorizedForJournal(email, exerciseJournal.getId());

        List<ExerciseList> journalList = findExerciseListByJournal(exerciseJournal);
        List<UserJournalDetailExerciseListDto> exerciseListDtos = userJournalDetailExerciseListDtos(journalList);
        UserJournalDetailDto userJournalDetailDto = new UserJournalDetailDto(exerciseJournal, exerciseListDtos);

        return new UserJournalDetailResponse(userJournalDetailDto);
    }

    /**
     * 운동 목록들을 DTO로 변환
     */
    private List<UserJournalDetailExerciseListDto> userJournalDetailExerciseListDtos(
            List<ExerciseList> journalList
    ) {
        return journalList.stream()
                .map(exerciseList ->
                        new UserJournalDetailExerciseListDto(
                                exerciseList,
                                exerciseService.findExercisePartByExercise(exerciseList.getExercise()),
                                userJournalDetailExerciseHistoryDtos(exerciseList)
                        ))
                .toList();
    }

    /**
     * 운동 기록을 DTO로 변환
     */
    private List<UserJournalDetailExerciseHistoryDto> userJournalDetailExerciseHistoryDtos(ExerciseList exerciseList) {
        return findExerciseHistoryByExerciseList(exerciseList)
                .stream()
                .map(UserJournalDetailExerciseHistoryDto::new)
                .toList();
    }

    /**
     * 운동일지로 운동 목록 찾기
     */
    private List<ExerciseList> findExerciseListByJournal(ExerciseJournal exerciseJournal) {
        return exerciseListRepository.findByExerciseJournal(exerciseJournal);
    }

    /**
     * 운동목록으로 운동기록 찾기
     */
    private List<ExerciseHistory> findExerciseHistoryByExerciseList(
            ExerciseList exerciseList
    ) {
        return exerciseHistoryRepository.findAllByExerciseList(exerciseList);
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
     * 유저와 일지날자로
     * 일지조회
     */
    private ExerciseJournal findExerciseJournalByUserAndJournalDate(
            User user, LocalDate journalDate
    ) {
        return exerciseJournalRepository.findByJournalDateAndUser(journalDate, user)
                .orElseThrow(NotFoundExerciseJournal::new);
    }

    /**
     * 일지 목록 검색
     * 일지 목록 ID로 일지목록 검색
     */
    private ExerciseList findExerciseList(Long exerciseListId) {
        return exerciseListRepository.findById(exerciseListId)
                .orElseThrow(NotFoundExerciseList::new);
    }

    /**
     * 운동기록 Id로 운동기록 검색
     */
    private ExerciseHistory findExerciseHistoryById(Long exerciseHistoryId) {
        return exerciseHistoryRepository.findById(exerciseHistoryId)
                .orElseThrow(NotFoundExerciseHistory::new);
    }

    /**
     * 유저검색
     */
    private User findUserByEmail(String email) {
        return userRepository.findById(email)
                .orElseThrow(NotFoundUser::new);
    }
}
