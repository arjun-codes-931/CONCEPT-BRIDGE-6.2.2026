package com.concept.conceptbridge.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/student/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Get all projects for student
    @GetMapping("/list")
    public List<StudentProject> getStudentProjects(@RequestParam Long studentId) {
        return projectService.getStudentProjects(studentId);
    }
    // DELETE PROJECT
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🗑️ DELETE request received for project ID: " + projectId);

            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            System.out.println("👤 Authenticated user: " + username);

            // Optional: Check if the project belongs to this user
            // You may want to add this check in service

            // Call service method to delete
            projectService.deleteProject(projectId);

            // Return success response
            response.put("success", true);
            response.put("message", "Project deleted successfully");
            response.put("projectId", projectId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error deleting project: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("error", "Failed to delete project");
            response.put("message", e.getMessage());

            // Return 400 for bad request, 403 for permission issues
            if (e.getMessage() != null && e.getMessage().contains("permission")) {
                return ResponseEntity.status(403).body(response);
            }

            return ResponseEntity.badRequest().body(response);
        }
    }
    // Get project with details (WITH tech stack)
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProject(@PathVariable Long projectId) {
        try {
            ProjectService.ProjectResponse response = projectService.getProjectWithTech(projectId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create new project
    @PostMapping("/create")
    public ResponseEntity<?> createProject(
            @RequestParam Long studentId,
            @RequestBody ProjectService.ProjectRequest request) {
        try {
            StudentProject project = projectService.createProject(studentId, request);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Update project
    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(
            @PathVariable Long projectId,
            @RequestBody ProjectService.ProjectRequest request) {
        try {
            StudentProject project = projectService.updateProject(projectId, request);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // DELETE THIS ENTIRE METHOD - IT'S CAUSING THE CONFLICT
    // @GetMapping("/{projectIds}")
    // public Optional<StudentProject> getProjectStudent(@PathVariable Long projectIds) {
    //     return projectService.getProjectStudent(projectIds);
    // }
}