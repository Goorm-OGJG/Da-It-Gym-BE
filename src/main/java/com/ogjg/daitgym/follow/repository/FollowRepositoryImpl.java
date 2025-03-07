package com.ogjg.daitgym.follow.repository;

import com.ogjg.daitgym.follow.dto.response.FollowListDto;
import com.ogjg.daitgym.follow.dto.response.QFollowListDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ogjg.daitgym.domain.QUser.user;
import static com.ogjg.daitgym.domain.follow.QFollow.follow;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FollowListDto> followingList(String nickname) {
        return jpaQueryFactory
                .select(
                        new QFollowListDto(
                                user.email,
                                user.imageUrl,
                                user.nickname,
                                user.introduction
                        ))
                .from(follow)
                .join(follow.target, user)
                .where(follow.follower.nickname.eq(nickname))
                .fetch();
    }

    @Override
    public List<FollowListDto> followerList(String nickname) {
        return jpaQueryFactory
                .select(
                        new QFollowListDto(
                                user.email,
                                user.imageUrl,
                                user.nickname,
                                user.introduction
                        ))
                .from(follow)
                .join(follow.follower, user)
                .where(follow.target.nickname.eq(nickname))
                .fetch();
    }
}
