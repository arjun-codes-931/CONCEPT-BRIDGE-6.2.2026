package com.concept.conceptbridge.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher/assignment")
public class TeacherAssignmentController {

    @Autowired
    private TeacherAssignmentService teacherAssignmentService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherAssignmentController.class);
    // GET all assignments by teacher
    @GetMapping
    public ResponseEntity<List<Assignment>> getAssignments(
            @RequestParam Long teacherId) {
        List<Assignment> assignments = teacherAssignmentService.getAssignmentsByTeacher(teacherId);
        return ResponseEntity.ok(assignments);
    }

    // CREATE new assignment
//    @PostMapping
//    public ResponseEntity<Assignment> createAssignment(
//            @RequestBody Assignment assignment) {
//        Assignment created = teacherAssignmentService.createAssignment(assignment);
//        return ResponseEntity.ok(created);
//    }

    // CREATE new assignment
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @RequestBody Assignment assignment) {
        logger.info("Creating assignment: {} for teacher: {}",
                assignment.getTitle(), assignment.getCreatedBy());
        Assignment created = teacherAssignmentService.createAssignment(assignment);
        return ResponseEntity.ok(created);
    }

    // GET assignment details
    @GetMapping("/{assignmentId}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable Long assignmentId) {
        Assignment assignment = teacherAssignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    // GET assignment statistics
    @GetMapping("/{assignmentId}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long assignmentId) {
        Map<String, Object> stats = teacherAssignmentService.getAssignmentStats(assignmentId);
        return ResponseEntity.ok(stats);
    }

    // DELETE assignment
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
        teacherAssignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.ok().build();
    }

    // TEST endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Teacher Assignment API is working");
    }
}