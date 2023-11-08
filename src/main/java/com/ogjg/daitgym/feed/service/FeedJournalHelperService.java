package com.ogjg.daitgym.feed.service;


import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournal;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.user.exception.NotFoundUser;
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

    public User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(NotFoundUser::new);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUser::new);
    }


}
