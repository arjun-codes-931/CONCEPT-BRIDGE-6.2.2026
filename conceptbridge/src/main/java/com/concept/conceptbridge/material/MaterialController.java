package com.concept.conceptbridge.material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    // 1. Get PDF list for student
    @GetMapping("/pdfs")
    public List<Material> getAvailablePdfsForStudent(
            @RequestParam String branch,
            @RequestParam Integer semester) {
        return materialService.getAvailablePdfsForStudent(branch, semester);
    }
    @DeleteMapping("/pdf/{id}")
    public ResponseEntity<?> deletePdf(@PathVariable Long id) {

        materialService.deleteMaterial(id);

        return ResponseEntity.ok("PDF deleted successfully");
    }
    // 2. Get PDF as base64 (WORKING - no CORS/auth issues)
    @GetMapping("/pdf/base64/{id}")
    public ResponseEntity<Map<String, Object>> getPdfBase64(@PathVariable Long id) {
        try {
            Map<String, Object> response = materialService.getPdfBase64(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

}