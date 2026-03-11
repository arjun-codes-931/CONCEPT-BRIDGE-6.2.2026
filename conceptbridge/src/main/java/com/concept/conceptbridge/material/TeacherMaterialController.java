package com.concept.conceptbridge.material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher/material")  // Separate path for teachers
public class TeacherMaterialController {

    @Autowired
    private MaterialService materialService;

    // Get PDFs uploaded by teacher
    @GetMapping("/pdfs")
    public List<Material> getMaterialsByTeacher(@RequestParam Long teacherId) {
        return materialService.getMaterialsByTeacher(teacherId);
    }

    // Upload PDF
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "semester", required = false) Integer semester,
            @RequestParam("teacherId") Long teacherId) {

        try {
            Material material = materialService.uploadMaterial(
                    file, title, description, subject, branch, semester, teacherId
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "PDF uploaded successfully");
            response.put("material", material);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Delete PDF
    @DeleteMapping("/{materialId}")
    public ResponseEntity<Map<String, Object>> deleteMaterial(@PathVariable Long materialId) {
        try {
            materialService.deleteMaterial(materialId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "PDF deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Teacher Material API is working!");
    }
}