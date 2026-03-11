package com.concept.conceptbridge.project;

import com.concept.conceptbridge.student.StudentProfile;
import com.concept.conceptbridge.student.StudentProfileRepository;
import com.concept.conceptbridge.teacher.TeacherProfile;
import com.concept.conceptbridge.teacher.TeacherProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher")
public class TeacherProjectController {

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @GetMapping("/by-username")
    public ResponseEntity<?> getTeacherByUsername(@RequestParam String username) {
        try {
            System.out.println("🔍 Searching for teacher with username: " + username);

            // Get teacher by username
            TeacherProfile teacher = teacherProfileRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Teacher not found with username: " + username));

            System.out.println("✅ Found teacher: " + teacher.getFullName());
            System.out.println("   Department: " + teacher.getDepartment());
            System.out.println("   Teacher ID: " + teacher.getId());

            // Get students from same department
            List<StudentProfile> students = studentProfileRepository.findByBranch(teacher.getDepartment());
            System.out.println("📚 Found " + students.size() + " students in " + teacher.getDepartment() + " department");

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("teacherId", teacher.getId());
            response.put("teacherName", teacher.getFullName());
            response.put("department", teacher.getDepartment());
            response.put("studentCount", students.size());

            // Create student list with all necessary info
            List<Map<String, Object>> studentList = students.stream()
                    .map(student -> {
                        Map<String, Object> studentMap = new HashMap<>();
                        studentMap.put("id", student.getId());
                        studentMap.put("fullName", student.getFullName());
                        studentMap.put("rollNumber", student.getRollNumber());
                        studentMap.put("branch", student.getBranch());
                        studentMap.put("semester", student.getSemester());
                        studentMap.put("section", student.getSection());

                        // Add user ID from associated user
                        if (student.getUser() != null) {
                            studentMap.put("userId", student.getUser().getId());
                            studentMap.put("username", student.getUser().getUsername());
                        }

                        return studentMap;
                    })
                    .collect(Collectors.toList());

            response.put("students", studentList);

            System.out.println("✅ Response prepared with " + studentList.size() + " students");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error in getTeacherByUsername: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to load teacher data");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Alternative endpoint using teacher ID
    @GetMapping("/students")
    public ResponseEntity<?> getTeacherStudents(@RequestParam Long teacherId) {
        try {
            System.out.println("🔍 Finding teacher with ID: " + teacherId);

            TeacherProfile teacher = teacherProfileRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

            System.out.println("✅ Found teacher: " + teacher.getFullName() + " in " + teacher.getDepartment());

            List<StudentProfile> students = studentProfileRepository.findByBranch(teacher.getDepartment());

            Map<String, Object> response = new HashMap<>();
            response.put("teacherName", teacher.getFullName());
            response.put("department", teacher.getDepartment());
            response.put("studentCount", students.size());

            List<Map<String, Object>> studentList = students.stream()
                    .map(student -> {
                        Map<String, Object> studentMap = new HashMap<>();
                        studentMap.put("id", student.getId());
                        studentMap.put("fullName", student.getFullName());
                        studentMap.put("rollNumber", student.getRollNumber());
                        studentMap.put("branch", student.getBranch());
                        studentMap.put("semester", student.getSemester());
                        return studentMap;
                    })
                    .collect(Collectors.toList());

            response.put("students", studentList);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}