package com.ogjg.daitgym.follow.service;

import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.follow.Follow;
import com.ogjg.daitgym.follow.dto.request.FollowRequest;
import com.ogjg.daitgym.follow.exception.AlreadyFollowUser;
import com.ogjg.daitgym.follow.exception.NotFoundFollow;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.user.exception.NotFoundUser;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /**
     * 팔로우
     */
    @Transactional
    public void follow(String email, FollowRequest followRequest) {
        User user = findUserByEmail(email);
        User targetUser = findUserByEmail(followRequest.getEmail());
        Follow.PK followPK = Follow.createFollowPK(followRequest.getEmail(), email);

        if (followRepository.findById(followPK).isPresent())
            throw new AlreadyFollowUser();

        followRepository.save(
                new Follow(followPK, targetUser, user)
        );
    }

    /**
     * 언팔로우
     */
    @Transactional
    public void unfollow(String email, FollowRequest followRequest) {
        findUserByEmail(followRequest.getEmail());
        Follow.PK followPK = Follow.createFollowPK(followRequest.getEmail(), email);
        findFollowByFollowPK(followPK);
        followRepository.deleteById(followPK);
    }

    /**
     * 이메일로 유저 찾기
     */
    private User findUserByEmail(String email) {
        return userRepository.findById(email)
                .orElseThrow(NotFoundUser::new);
    }

    /**
     * Follow 찾기
     */
    private void findFollowByFollowPK(
            Follow.PK followPk
    ) {
        followRepository.findById(followPk)
                .orElseThrow(NotFoundFollow::new);
    }


}
