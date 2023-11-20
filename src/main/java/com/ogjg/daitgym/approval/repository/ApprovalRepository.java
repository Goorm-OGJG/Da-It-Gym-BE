package com.ogjg.daitgym.approval.repository;

import com.ogjg.daitgym.domain.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    @Query(value = """
    SELECT * FROM
    (
    SELECT a.content, a.approval_manager, a.created_at , a.modified_at, a.id as id, a.approve_status as approve_status 
        FROM approval a
        INNER JOIN certification c ON a.id = c.approval_id
        INNER JOIN users u ON c.email = u.email
        WHERE u.nickname LIKE :nickname%
    UNION
    SELECT a.content, a.approval_manager, a.created_at , a.modified_at, a.id as id, a.approve_status as approve_status 
        FROM approval a
        INNER JOIN award aw ON a.id = aw.approval_id
        INNER JOIN users u ON aw.email = u.email
        WHERE u.nickname LIKE :nickname%
    )
    AS mytable ORDER BY mytable.approve_status ASC, mytable.id ASC LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Approval> findByUserNickname(@Param("nickname") String nickname, @Param("limit") int limit, @Param("offset") int offset);


    @Query(value = """
    SELECT COUNT(*) FROM
    (
    SELECT a.id 
        FROM approval a
        INNER JOIN certification c ON a.id = c.approval_id
        INNER JOIN users u ON c.email = u.email
        WHERE u.nickname LIKE :nickname%
    UNION
    SELECT a.id 
        FROM approval a
        INNER JOIN award aw ON a.id = aw.approval_id
        INNER JOIN users u ON aw.email = u.email
        WHERE u.nickname LIKE :nickname%
    ) AS mytable
    """, nativeQuery = true)
    long countTotalPageByUserNickname(@Param("nickname") String nickname);
}


