package com.ogjg.daitgym.journal.repository.journal;

import com.ogjg.daitgym.domain.User;
import com.ogjg.daitgym.domain.journal.ExerciseJournal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExerciseJournalRepository extends JpaRepository<ExerciseJournal, Long>, ExerciseJournalRepositoryCustom {

    List<ExerciseJournal> findAllByUser(User user);

    Optional<ExerciseJournal> findByJournalDateAndUser(LocalDate journalDate, User user);

    int countByUserAndIsCompleted(User user, boolean completed);

    int countByUserEmail(String email);
}
