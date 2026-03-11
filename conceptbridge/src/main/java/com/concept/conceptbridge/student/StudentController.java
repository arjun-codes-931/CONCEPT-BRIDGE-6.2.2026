package com.concept.conceptbridge.student;

import com.concept.conceptbridge.student.dto.StudentProfileUpdateRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/profile")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public StudentProfile myProfile() {
        return studentService.getMyProfile();
    }

    @PutMapping
    public StudentProfile update(@RequestBody StudentProfileUpdateRequest request) {
        return studentService.updatePersonalInfo(request.getFullName());
    }
}
