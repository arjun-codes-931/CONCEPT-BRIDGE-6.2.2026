package com.concept.conceptbridge.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentQuizAnswerRepository
        extends JpaRepository<StudentQuizAnswer, Long> {
    List<StudentQuizAnswer> findByAttemptId(Long attemptId);

    Optional<StudentQuizAnswer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);
}