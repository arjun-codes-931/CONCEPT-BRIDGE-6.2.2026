package com.concept.conceptbridge.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/student/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // Get all assignments for student
    @GetMapping("/list")
    public List<AssignmentService.AssignmentWithStatus> getAssignmentsForStudent(
            @RequestParam Long studentId,
            @RequestParam String branch,
            @RequestParam Integer semester) {
        return assignmentService.getAssignmentsForStudent(studentId, branch, semester);
    }

    // Get assignment details
    @GetMapping("/{id}")
    public Assignment getAssignmentDetails(@PathVariable Long id) {
        return assignmentService.getAssignmentDetails(id);
    }

    // Mark assignment as viewed
    @PostMapping("/view/{assignmentId}")
    public ResponseEntity<String> markAsViewed(
            @PathVariable Long assignmentId,
            @RequestParam Long studentId) {
        assignmentService.markAsViewed(studentId, assignmentId);
        return ResponseEntity.ok("Marked as viewed");
    }

    // Mark assignment as submitted
    @PostMapping("/submit/{assignmentId}")
    public ResponseEntity<String> markAsSubmitted(
            @PathVariable Long assignmentId,
            @RequestParam Long studentId) {
        assignmentService.markAsSubmitted(studentId, assignmentId);
        return ResponseEntity.ok("Marked as submitted");
    }
}