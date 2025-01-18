package com.ogjg.daitgym.feed.service;

import com.ogjg.daitgym.config.security.details.OAuth2JwtUserDetails;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListDto;
import com.ogjg.daitgym.feed.dto.response.FeedExerciseJournalListResponse;
import com.ogjg.daitgym.feed.repository.FeedExerciseJournalRepository;
import com.ogjg.daitgym.user.service.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFeedExerciseJournalService {

    private final UserHelper userHelper;
    private final FeedExerciseJournalRepository feedExerciseJournalRepository;
    private final FeedJournalHelper feedJournalHelper;

    /**
     * 유저 페이지 피드 운동일지 목록 가져오기
     */
    public FeedExerciseJournalListResponse userFeedExerciseJournalLists(
            String nickname, Pageable pageable,
            OAuth2JwtUserDetails userDetails
    ) {
        User loginUser = userHelper.findUserByEmail(userDetails.getEmail());
        Page<Long> userFeedExerciseJournals = feedExerciseJournalRepository.userFeedExerciseJournalListsOfUser(nickname, pageable, isMyFeedList(nickname, loginUser));

        List<FeedExerciseJournalListDto> content =
                feedJournalHelper.feedExerciseJournalsChangeFeedExerciseJournalsDto(userFeedExerciseJournals);

        int totalpage = userFeedExerciseJournals.getTotalPages();
        if (!content.isEmpty()) totalpage -= 1;

        return new FeedExerciseJournalListResponse(totalpage, content);
    }

    private boolean isMyFeedList(String nickname, User loginUser) {
        return nickname.equals(loginUser.getNickname());
    }

    /**
     * 피드 운동일지 보관함 조회 목록보기
     */
    public FeedExerciseJournalListResponse userFeedExerciseJournalCollectionLists(
            String nickname, Pageable pageable
    ) {
        Page<Long> userFeedJournalCollections =
                feedExerciseJournalRepository.userFeedExerciseJournalCollectionLists(nickname, pageable);

        List<FeedExerciseJournalListDto> content =
                feedJournalHelper.feedExerciseJournalsChangeFeedExerciseJournalsDto(userFeedJournalCollections);

        int totalpage = userFeedJournalCollections.getTotalPages();
        if (!content.isEmpty()) totalpage -= 1;

        return new FeedExerciseJournalListResponse(totalpage, content);
    }
}
