package com.ogjg.daitgym.like.feedExerciseJournal.service;

import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundFeedJournal;
import com.ogjg.daitgym.comment.feedExerciseJournal.exception.NotFoundUser;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.domain.feed.FeedExerciseJournalLike;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.like.feedExerciseJournal.dto.FeedExerciseJournalLikeResponse;
import com.ogjg.daitgym.like.feedExerciseJournal.repository.FeedExerciseJournalLikeRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedExerciseJournalLikeService {

    private final UserRepository userRepository;
    private final FeedExerciseJournalRepository feedJournalRepository;
    private final FeedExerciseJournalLikeRepository feedJournalLikeRepository;


    @Transactional
    public FeedExerciseJournalLikeResponse feedJournalLike(Long feedJournalId) {
        User user = getUserByEmail("yaejingo@gmail.com");
        FeedExerciseJournal feedExerciseJournal = feedJournalRepository.findById(feedJournalId).orElseThrow(NotFoundFeedJournal::new);


        if (!feedJournalLikeRepository.existsByUserEmailAndFeedExerciseJournalId(user.getEmail(),feedJournalId)) {
            feedJournalLikeRepository.save(new FeedExerciseJournalLike(user, feedExerciseJournal));
        }
        int likeCount = feedJournalLikeRepository.countByFeedJournalLikePkFeedExerciseJournalId(feedJournalId);
        return new FeedExerciseJournalLikeResponse(likeCount);
    }

    @Transactional
    public FeedExerciseJournalLikeResponse feedJournalUnLike(Long feedJournalId) {
        User user = getUserByEmail("yaejingo@gmail.com");
        FeedExerciseJournal feedExerciseJournal = feedJournalRepository.findById(feedJournalId).orElseThrow(NotFoundFeedJournal::new);


        if (feedJournalLikeRepository.existsByUserEmailAndFeedExerciseJournalId(user.getEmail(),feedJournalId)) {
            feedJournalLikeRepository.delete(new FeedExerciseJournalLike(user, feedExerciseJournal));
        }
        int likeCount = feedJournalLikeRepository.countByFeedJournalLikePkFeedExerciseJournalId(feedJournalId);

        return new FeedExerciseJournalLikeResponse(likeCount);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUser::new);
    }
}
