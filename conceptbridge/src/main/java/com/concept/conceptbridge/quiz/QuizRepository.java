package com.concept.conceptbridge.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("""
    SELECT q FROM Quiz q 
    WHERE q.branch = :branch 
    AND q.semester = :semester 
    AND (:section IS NULL OR q.section = :section)
    ORDER BY q.startTime ASC
""")
    List<Quiz> findVisibleQuizzes(
            @Param("branch") String branch,
            @Param("semester") Integer semester,
            @Param("section") String section
    );
}