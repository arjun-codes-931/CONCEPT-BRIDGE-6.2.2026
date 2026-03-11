package com.concept.conceptbridge.assignment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TeacherAssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentAssignmentStatusRepository statusRepository;

    // =========== TEACHER METHODS ===========

    public List<Assignment> getAssignmentsByTeacher(Long createdBy) {
        return assignmentRepository.findByCreatedByOrderByCreatedAtDesc(createdBy);
    }

    public Assignment createAssignment(Assignment assignment) {
        assignment.setCreatedAt(LocalDateTime.now());
        return assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public Map<String, Object> getAssignmentStats(Long assignmentId) {
        Map<String, Object> stats = new HashMap<>();

        // Get all statuses for this assignment
        List<StudentAssignmentStatus> allStatuses = statusRepository.findAll().stream()
                .filter(status -> status.getAssignmentId() != null && status.getAssignmentId().equals(assignmentId))
                .toList();

        long total = allStatuses.size();
        long viewed = allStatuses.stream().filter(s -> "VIEWED".equals(s.getStatus())).count();
        long submitted = allStatuses.stream().filter(s -> "SUBMITTED".equals(s.getStatus())).count();

        stats.put("totalStudents", total);
        stats.put("viewed", viewed);
        stats.put("submitted", submitted);
        stats.put("notViewed", total - viewed);

        return stats;
    }

    public void deleteAssignment(Long assignmentId) {
        // Delete statuses first
        List<StudentAssignmentStatus> statuses = statusRepository.findAll().stream()
                .filter(s -> s.getAssignmentId() != null && s.getAssignmentId().equals(assignmentId))
                .toList();

        if (!statuses.isEmpty()) {
            statusRepository.deleteAll(statuses);
        }

        // Delete assignment
        assignmentRepository.deleteById(assignmentId);
    }
}