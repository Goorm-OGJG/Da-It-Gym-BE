package com.ogjg.daitgym.follow.repository;

import com.ogjg.daitgym.follow.dto.response.FollowListDto;

import java.util.List;

public interface FollowRepositoryCustom {

    List<FollowListDto> followingList(String nickname);

    List<FollowListDto> followerList(String nickname);

}
