package com.ogjg.daitgym.feed.repository;


import com.ogjg.daitgym.domain.feed.FeedExerciseJournal;
import com.ogjg.daitgym.feed.dto.request.FeedSearchConditionRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ogjg.daitgym.domain.exercise.QExercise.exercise;
import static com.ogjg.daitgym.domain.exercise.QExercisePart.exercisePart;
import static com.ogjg.daitgym.domain.feed.QFeedExerciseJournal.feedExerciseJournal;
import static com.ogjg.daitgym.domain.follow.QFollow.follow;
import static com.ogjg.daitgym.domain.journal.QExerciseJournal.exerciseJournal;
import static com.ogjg.daitgym.domain.journal.QExerciseList.exerciseList;

@RequiredArgsConstructor
public class FeedExerciseJournalRepositoryImpl implements FeedExerciseJournalRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

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
                .leftJoin(feedExerciseJournal.exerciseJournal, exerciseJournal).fetchJoin()
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
                .leftJoin(feedExerciseJournal.exerciseJournal, exerciseJournal)
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
                .leftJoin(feedExerciseJournal).on(feedExerciseJournal.exerciseJournal.user.email.eq(follow.target.email)).fetchJoin()
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
                .leftJoin(feedExerciseJournal).on(feedExerciseJournal.exerciseJournal.user.email.eq(follow.target.email)).fetchJoin()
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
