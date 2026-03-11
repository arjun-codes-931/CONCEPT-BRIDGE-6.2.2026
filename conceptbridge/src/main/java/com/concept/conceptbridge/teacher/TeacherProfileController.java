package com.concept.conceptbridge.teacher;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/teacher/profile")
public class TeacherProfileController {

    private final TeacherProfileService teacherService;

    public TeacherProfileController(TeacherProfileService teacherService) {
        this.teacherService = teacherService;
    }

    // Get teacher profile
    @GetMapping
    public ResponseEntity<?> getProfile() {
        try {
            TeacherProfile profile = teacherService.getMyProfile();
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(
                    Map.of("error", "Teacher profile not found", "message", e.getMessage())
            );
        }
    }
    @GetMapping("/user-id")
    public ResponseEntity<?> getUserId() {
        try {
            TeacherProfile profile = teacherService.getMyProfile();

            if (profile.getUser() != null) {
                Map<String, Long> response = new HashMap<>();
                response.put("userId", profile.getUser().getId());
                System.out.println("✅ Returning only userId: " + profile.getUser().getId());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(404).body(
                        Map.of("error", "User not found for this teacher profile")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Failed to get user ID", "message", e.getMessage())
            );
        }
    }
    // Update teacher profile (only full name)
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> request) {
        try {
            String fullName = request.get("fullName");

            if (fullName == null || fullName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Full name is required")
                );
            }

            TeacherProfile updated = teacherService.updatePersonalInfo(fullName.trim());

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Profile updated successfully",
                            "profile", updated
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Update failed", "message", e.getMessage())
            );
        }
    }
}