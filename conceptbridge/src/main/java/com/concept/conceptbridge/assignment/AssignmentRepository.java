package com.concept.conceptbridge.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

// Add these methods to existing AssignmentRepository.java
@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // EXISTING student methods
    List<Assignment> findByBranchAndSemester(String branch, Integer semester);
    List<Assignment> findByBranchAndSemesterAndSubject(String branch, Integer semester, String subject);

    // NEW teacher methods
    List<Assignment> findByCreatedByOrderByCreatedAtDesc(Long createdBy);

    @Query("SELECT a FROM Assignment a WHERE a.createdBy = :createdBy " +
            "AND (:branch IS NULL OR a.branch = :branch) " +
            "AND (:subject IS NULL OR a.subject = :subject)")
    List<Assignment> findByTeacherWithFilters(
            @Param("createdBy") Long createdBy,
            @Param("branch") String branch,
            @Param("subject") String subject);
}