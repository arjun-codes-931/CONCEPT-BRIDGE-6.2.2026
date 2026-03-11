package com.concept.conceptbridge.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudentQuizAttemptRepository
        extends JpaRepository<StudentQuizAttempt, Long> {

    Optional<StudentQuizAttempt> findByStudentIdAndQuizId(
            Long studentId,
            Long quizId
    );

    List<StudentQuizAttempt> findByQuizId(Long quizId);

    long countByQuizId(Long quizId);

    // Optional: Filter by status
    List<StudentQuizAttempt> findByQuizIdAndStatus(Long quizId, AttemptStatus status);

}