package com.concept.conceptbridge.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAssignmentStatusRepository extends JpaRepository<StudentAssignmentStatus, Long> {
    Optional<StudentAssignmentStatus> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
    List<StudentAssignmentStatus> findByStudentId(Long studentId);
}