package com.ogjg.daitgym.follow.service;

import com.ogjg.daitgym.common.exception.follow.AlreadyFollowUser;
import com.ogjg.daitgym.common.exception.follow.NotFoundFollow;
import com.ogjg.daitgym.common.exception.user.NotFoundUser;
import com.ogjg.daitgym.domain.Inbody;
import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.follow.Follow;
import com.ogjg.daitgym.follow.dto.response.FollowCountResponse;
import com.ogjg.daitgym.follow.dto.response.FollowListDto;
import com.ogjg.daitgym.follow.dto.response.FollowListResponse;
import com.ogjg.daitgym.follow.repository.FollowRepository;
import com.ogjg.daitgym.user.repository.InbodyRepository;
import com.ogjg.daitgym.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final InbodyRepository inbodyRepository;

    /**
     * 팔로우
     */
    @Transactional
    public void follow(String email, String targetNickname) {
        User user = findUserByEmail(email);
        User targetUser = findUserByNickName(targetNickname);

        if (user.getEmail().equals(targetUser.getEmail())) {
            throw new AlreadyFollowUser("자신은 팔로우 할 수 없습니다");
        }

        Follow.PK followPK = Follow.createFollowPK(targetUser.getEmail(), email);

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
    public void unfollow(String email, String targetNickname) {
        User targetUser = findUserByNickName(targetNickname);
        Follow.PK followPK = Follow.createFollowPK(targetUser.getEmail(), email);
        findFollowByFollowPK(followPK);
        followRepository.deleteById(followPK);
    }

    /**
     * 나를 팔로우 하고있는 사람의 수
     */
    @Transactional(readOnly = true)
    public FollowCountResponse followerCount(String nickname) {
        User user = findUserByNickName(nickname);
        int followerCount = followRepository.countByFollowPKTargetEmail(user.getEmail());

        return new FollowCountResponse(followerCount);
    }

    /**
     * 내가 팔로우 하고있는 사람의 수
     */
    @Transactional(readOnly = true)
    public FollowCountResponse followingCount(String nickname) {
        User user = findUserByNickName(nickname);
        int followingCount = followRepository.countByFollowPKFollowerEmail(user.getEmail());

        return new FollowCountResponse(followingCount);
    }

    /**
     * 내가 팔로우한 사람들 목록
     */
    @Transactional(readOnly = true)
    public FollowListResponse followingList(String nickname) {
        User user = findUserByNickName(nickname);
        List<FollowListDto> followingList = followRepository.followingList(nickname);

        followingList.forEach(
                following -> following.putLatestInbodyScore(
                        userLatestInbodyScore(user.getEmail())
                )
        );

        return new FollowListResponse(followingList);
    }

    /**
     * 나를 팔로우한 사람들 목록
     */
    @Transactional(readOnly = true)
    public FollowListResponse followerList(String nickname) {
        User user = findUserByNickName(nickname);
        List<FollowListDto> followerList = followRepository.followerList(nickname);

        followerList.forEach(
                follower -> follower.putLatestInbodyScore(
                        userLatestInbodyScore(user.getEmail())
                )
        );

        return new FollowListResponse(followerList);
    }

    /**
     * 가장 최근의 인바디
     * @param email
     * @return
     */
    private int userLatestInbodyScore(String email) {
            return inbodyRepository.findFirstByUserEmailOrderByCreatedAtDesc(email)
                    .map(Inbody::getScore)
                    .orElse(0);
    }

    /**
     * 이메일로 유저 찾기
     */
    private User findUserByEmail(String email) {
        return userRepository.findById(email)
                .orElseThrow(NotFoundUser::new);
    }

    /**
     * 닉네임으로 유저 찾기
     */
    private User findUserByNickName(String nickname) {
        return userRepository.findByNickname(nickname)
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
