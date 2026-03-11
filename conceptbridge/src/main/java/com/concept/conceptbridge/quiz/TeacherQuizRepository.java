package com.concept.conceptbridge.quiz;

import com.concept.conceptbridge.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherQuizRepository extends JpaRepository<Quiz, Long> {

    // Find quizzes by createdBy (user ID)
    List<Quiz> findByCreatedByOrderByCreatedAtDesc(Long createdBy);

    @Query("SELECT q FROM Quiz q WHERE q.createdBy = :createdBy " +
            "AND (:branch IS NULL OR q.branch = :branch) " +
            "AND (:subject IS NULL OR q.subject = :subject)")
    List<Quiz> findByTeacherWithFilters(
            @Param("createdBy") Long createdBy,
            @Param("branch") String branch,
            @Param("subject") String subject);
}