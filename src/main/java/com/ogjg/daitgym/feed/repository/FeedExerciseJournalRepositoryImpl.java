package com.ogjg.daitgym.feed.repository;


import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import com.ogjg.daitgym.feed.dto.response.FeedDetailResponse;
import com.ogjg.daitgym.feed.dto.response.QFeedDetailResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.ogjg.daitgym.domain.QUser.user;
import static com.ogjg.daitgym.domain.exercise.QExercise.exercise;
import static com.ogjg.daitgym.domain.exercise.QExercisePart.exercisePart;
import static com.ogjg.daitgym.domain.feed.QFeedExerciseJournal.feedExerciseJournal;
import static com.ogjg.daitgym.domain.feed.QFeedExerciseJournalCollection.feedExerciseJournalCollection;
import static com.ogjg.daitgym.domain.follow.QFollow.follow;
import static com.ogjg.daitgym.domain.journal.QExerciseJournal.exerciseJournal;
import static com.ogjg.daitgym.domain.journal.QExerciseList.exerciseList;

@RequiredArgsConstructor
public class FeedExerciseJournalRepositoryImpl implements FeedExerciseJournalRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

//    todo left inner join인지 left outer join인지 생각보기

    /**
     * 피드의 운동일지 전체 목록 검색 query
     */
    @Override
    public Page<FeedExerciseJournal> feedExerciseJournalLists(
            Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest
    ) {
        List<FeedExerciseJournal> journalLists = jpaQueryFactory.select(
                        feedExerciseJournal
                ).from(feedExerciseJournal)
                .join(feedExerciseJournal.exerciseJournal, exerciseJournal).fetchJoin()
                .leftJoin(exerciseList).on(exerciseJournal.id.eq(exerciseList.exerciseJournal.id)).fetchJoin()
                .leftJoin(exercise).on(exerciseList.exercise.id.eq(exercise.id)).fetchJoin()
                .leftJoin(exercisePart).on(exercise.id.eq(exercisePart.exercise.id)).fetchJoin()
                .where(
                        feedExerciseJournal.exerciseJournal.split.eq(feedSearchConditionRequest.getSplit()),
                        exercisePartEq(feedSearchConditionRequest.getPart(), exercisePart.part)
                ).orderBy(feedExerciseJournal.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = jpaQueryFactory.select(feedExerciseJournal.count())
                .from(feedExerciseJournal)
                .join(feedExerciseJournal.exerciseJournal, exerciseJournal)
                .leftJoin(exerciseList).on(exerciseJournal.id.eq(exerciseList.exerciseJournal.id))
                .leftJoin(exercise).on(exerciseList.exercise.id.eq(exercise.id))
                .leftJoin(exercisePart).on(exercise.id.eq(exercisePart.exercise.id))
                .where(
                        feedExerciseJournal.exerciseJournal.split.eq(feedSearchConditionRequest.getSplit()),
                        exercisePartEq(feedSearchConditionRequest.getPart(), exercisePart.part)
                );

        return PageableExecutionUtils.getPage(journalLists, pageable, countQuery::fetchOne);
    }

    /*
     * 팔로우한 유저 피드 가져오기
     * 내가 팔로우의 target 사람들의 list를 가져와서 피드와 join
     * */
    @Override
    public Page<FeedExerciseJournal> feedExerciseJournalListsByFollow(
            String email, Pageable pageable, FeedSearchConditionRequest feedSearchConditionRequest
    ) {

        List<FeedExerciseJournal> followerFeedJournalLists = jpaQueryFactory.select(feedExerciseJournal)
                .from(follow)
                .where(follow.follower.email.eq(email))
                .join(feedExerciseJournal).on(feedExerciseJournal.exerciseJournal.user.email.eq(follow.target.email)).fetchJoin()
                .leftJoin(feedExerciseJournal.exerciseJournal, exerciseJournal).fetchJoin()
                .leftJoin(exerciseList).on(exerciseList.exerciseJournal.id.eq(exerciseJournal.id)).fetchJoin()
                .leftJoin(exercise).on(exerciseList.exercise.id.eq(exercise.id)).fetchJoin()
                .leftJoin(exercisePart).on(exercise.id.eq(exercisePart.exercise.id)).fetchJoin()
                .where(
                        exerciseJournal.split.eq(feedSearchConditionRequest.getSplit()),
                        exercisePartEq(feedSearchConditionRequest.getPart(), exercisePart.part)
                )
                .orderBy(feedExerciseJournal.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(
                        feedExerciseJournal.count()
                ).from(follow)
                .where(follow.follower.email.eq(email))
                .join(feedExerciseJournal).on(feedExerciseJournal.exerciseJournal.user.email.eq(follow.target.email)).fetchJoin()
                .leftJoin(feedExerciseJournal.exerciseJournal, exerciseJournal).fetchJoin()
                .leftJoin(exerciseList).on(exerciseList.exerciseJournal.id.eq(exerciseJournal.id)).fetchJoin()
                .leftJoin(exercise).on(exerciseList.exercise.id.eq(exercise.id)).fetchJoin()
                .leftJoin(exercisePart).on(exercise.id.eq(exercisePart.exercise.id)).fetchJoin()
                .where(
                        exerciseJournal.split.eq(feedSearchConditionRequest.getSplit()),
                        exercisePartEq(feedSearchConditionRequest.getPart(), exercisePart.part)
                );

        return PageableExecutionUtils.getPage(followerFeedJournalLists, pageable, countQuery::fetchOne);
    }

    /**
     * 유저 페이지 피드 운동일지 목록 가져오기
     */
    public Page<FeedExerciseJournal> userFeedExerciseJournalLists(
            String nickname, Pageable pageable
    ) {
        List<FeedExerciseJournal> userFeedJournalLists = jpaQueryFactory.select(feedExerciseJournal)
                .from(user)
                .where(user.nickname.eq(nickname))
                .join(feedExerciseJournal).on(user.email.eq(feedExerciseJournal.exerciseJournal.user.email)).fetchJoin()
                .orderBy(feedExerciseJournal.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(feedExerciseJournal.count())
                .from(user)
                .where(user.nickname.eq(nickname))
                .leftJoin(feedExerciseJournal).on(user.email.eq(feedExerciseJournal.exerciseJournal.user.email));

        return PageableExecutionUtils.getPage(userFeedJournalLists, pageable, countQuery::fetchOne);
    }

    /**
     * 유저 페이지 피드 운동일지 컬렉션 가져오기
     */
    public Page<FeedExerciseJournal> userFeedExerciseJournalCollectionLists(
            String nickname, Pageable pageable
    ) {
        List<FeedExerciseJournal> userFeedJournalCollections = jpaQueryFactory.select(feedExerciseJournal)
                .from(feedExerciseJournalCollection)
                .join(user).on(feedExerciseJournalCollection.user.email.eq(user.email)).fetchJoin()
                .where(user.nickname.eq(nickname))
                .join(feedExerciseJournal).on(feedExerciseJournalCollection.feedExerciseJournal.id.eq(feedExerciseJournal.id)).fetchJoin()
                .join(exerciseJournal).on(feedExerciseJournal.exerciseJournal.id.eq(exerciseJournal.id)).fetchJoin()
                .orderBy(feedExerciseJournal.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(feedExerciseJournal.count())
                .from(feedExerciseJournalCollection)
                .join(user).on(feedExerciseJournalCollection.user.email.eq(user.email)).fetchJoin()
                .where(user.nickname.eq(nickname))
                .join(feedExerciseJournal).on(feedExerciseJournalCollection.feedExerciseJournal.id.eq(feedExerciseJournal.id)).fetchJoin()
                .join(exerciseJournal).on(feedExerciseJournal.exerciseJournal.id.eq(exerciseJournal.id)).fetchJoin();

        return PageableExecutionUtils.getPage(userFeedJournalCollections, pageable, countQuery::fetchOne);
    }

    /**
     * 피드 운동일지 피드부분 상세보기
     */
    public Optional<FeedDetailResponse> feedDetail(
            Long feedJournalId
    ) {
        FeedDetailResponse result = jpaQueryFactory
                .select(
                        new QFeedDetailResponse(
                                feedExerciseJournal.id,
                                user.nickname,
                                user.imageUrl,
                                feedExerciseJournal.createdAt
                        )
                )
                .from(feedExerciseJournal)
                .join(feedExerciseJournal.exerciseJournal, exerciseJournal)
                .join(user).on(exerciseJournal.user.email.eq(user.email))
                .where(feedExerciseJournal.id.eq(feedJournalId))
                .fetchOne();

        return Optional.ofNullable(result);
    }


    /**
     * 운동 부위의 검색목록이 들어올시
     * or문으로 연산이 실행 검색목록이 빈배열시
     * null을 반환하며 실행안됌
     */
    private BooleanExpression exercisePartEq(List<String> parts, StringPath queryPart) {
        if (parts == null) return null;

        return parts.stream()
                .map(queryPart::eq)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
