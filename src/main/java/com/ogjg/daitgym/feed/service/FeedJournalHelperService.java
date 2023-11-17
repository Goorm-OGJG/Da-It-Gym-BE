package com.ogjg.daitgym.feed.service;


import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournal;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedJournalHelperService {

    private final FeedExerciseJournalRepository feedExerciseJournalRepository;
    private final UserRepository userRepository;

    /**
     * Id로 feedJournal 검색
     */
    public FeedExerciseJournal findFeedJournalById(Long feedJournalId) {
        return feedExerciseJournalRepository.findById(feedJournalId)
                .orElseThrow(NotFoundFeedJournal::new);
    }

    /**
     * 운동일지로 피드운동일지가 존재하는지 확인하기
     */
    public boolean checkExistFeedExerciseJournalByExerciseJournal(ExerciseJournal exerciseJournal){
        return feedExerciseJournalRepository.findByExerciseJournal(exerciseJournal).isPresent();
    }

    public User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(NotFoundUser::new);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUser::new);
    }


}
