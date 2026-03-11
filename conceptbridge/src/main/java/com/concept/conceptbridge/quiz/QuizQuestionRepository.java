package com.concept.conceptbridge.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuizQuestionRepository
        extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findByQuizIdOrderByIdAsc(Long quizId);
    List<QuizQuestion> findByQuizId(Long quizId);
}