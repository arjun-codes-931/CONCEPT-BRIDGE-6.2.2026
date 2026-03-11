package com.concept.conceptbridge.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentAssignmentStatusRepository statusRepository;

    // Get assignments for student with status
    public List<AssignmentWithStatus> getAssignmentsForStudent(Long studentId, String branch, Integer semester) {
        // Get all assignments for student's branch and semester
        List<Assignment> assignments = assignmentRepository.findByBranchAndSemester(branch, semester);

        return assignments.stream().map(assignment -> {
            AssignmentWithStatus dto = new AssignmentWithStatus();
            dto.setAssignment(assignment);

            // Check student's status for this assignment
            statusRepository.findByStudentIdAndAssignmentId(studentId, assignment.getId())
                    .ifPresent(status -> {
                        dto.setStatus(status.getStatus());
                        dto.setViewedAt(status.getViewedAt());
                        dto.setSubmittedAt(status.getSubmittedAt());
                        dto.setMarksObtained(status.getMarksObtained());
                    });

            return dto;
        }).collect(Collectors.toList());
    }

    // Get assignment details
    public Assignment getAssignmentDetails(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    // Mark as viewed
    public void markAsViewed(Long studentId, Long assignmentId) {
        StudentAssignmentStatus status = statusRepository
                .findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElse(new StudentAssignmentStatus());

        status.setStudentId(studentId);
        status.setAssignmentId(assignmentId);
        status.setStatus("VIEWED");
        status.setViewedAt(LocalDateTime.now());

        statusRepository.save(status);
    }

    // Mark as submitted
    public void markAsSubmitted(Long studentId, Long assignmentId) {
        StudentAssignmentStatus status = statusRepository
                .findByStudentIdAndAssignmentId(studentId, assignmentId)
                .orElse(new StudentAssignmentStatus());

        status.setStudentId(studentId);
        status.setAssignmentId(assignmentId);
        status.setStatus("SUBMITTED");
        status.setSubmittedAt(LocalDateTime.now());

        statusRepository.save(status);
    }

    // DTO for assignment with status
    public static class AssignmentWithStatus {
        private Assignment assignment;
        private String status = "NOT_VIEWED";
        private LocalDateTime viewedAt;
        private LocalDateTime submittedAt;
        private Integer marksObtained;

        // Getters and setters
        public Assignment getAssignment() { return assignment; }
        public void setAssignment(Assignment assignment) { this.assignment = assignment; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDateTime getViewedAt() { return viewedAt; }
        public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }

        public LocalDateTime getSubmittedAt() { return submittedAt; }
        public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

        public Integer getMarksObtained() { return marksObtained; }
        public void setMarksObtained(Integer marksObtained) { this.marksObtained = marksObtained; }
    }
}