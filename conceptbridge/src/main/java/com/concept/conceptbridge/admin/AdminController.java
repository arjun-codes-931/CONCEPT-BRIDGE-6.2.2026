package com.concept.conceptbridge.admin;

import com.concept.conceptbridge.entity.ProfileStatus;
import com.concept.conceptbridge.entity.User;
import com.concept.conceptbridge.material.Material;
import com.concept.conceptbridge.material.MaterialService;
import com.concept.conceptbridge.project.*;
import com.concept.conceptbridge.project.StudentProjectRepository;
import com.concept.conceptbridge.quiz.TeacherQuizService;
import com.concept.conceptbridge.repository.UserRepository;
import com.concept.conceptbridge.student.StudentProfile;
import com.concept.conceptbridge.student.StudentProfileRepository;
import com.concept.conceptbridge.teacher.TeacherProfile;
import com.concept.conceptbridge.teacher.TeacherProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
@Autowired
private StudentProjectRepository studentProjectRepository;
    @Autowired
    private ProjectTechStackRepository techStackRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentRepo;

    @Autowired
    private TeacherProfileRepository teacherRepo;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TeacherQuizService teacherQuizService;

    // ==================== INNER DTO CLASSES ====================

    // Student with Projects DTO
    public static class StudentWithProjectsDTO {
        private StudentProfile student;
        private List<StudentProjectDTO> projects;

        public StudentWithProjectsDTO() {}

        public StudentWithProjectsDTO(StudentProfile student, List<StudentProjectDTO> projects) {
            this.student = student;
            this.projects = projects;
        }

        public StudentProfile getStudent() { return student; }
        public void setStudent(StudentProfile student) { this.student = student; }

        public List<StudentProjectDTO> getProjects() { return projects; }
        public void setProjects(List<StudentProjectDTO> projects) { this.projects = projects; }
    }

    // Student Project with Tech Stack DTO
    public static class StudentProjectDTO {
        private Long id;
        private String title;
        private String description;
        private String category;
        private String currentStatus;
        private Integer progressPercentage;
        private String githubRepo;
        private String liveDemoUrl;
        private String startDate;
        private String expectedEndDate;
        private String lastUpdated;
        private List<ProjectTechStackDTO> techStack;

        public StudentProjectDTO() {}

        public StudentProjectDTO(StudentProject project, List<ProjectTechStackDTO> techStack) {
            this.id = project.getId();
            this.title = project.getTitle();
            this.description = project.getDescription();
            this.category = project.getCategory();
            this.currentStatus = project.getCurrentStatus();
            this.progressPercentage = project.getProgressPercentage();
            this.githubRepo = project.getGithubRepo();
            this.liveDemoUrl = project.getLiveDemoUrl();
            this.startDate = project.getStartDate() != null ? project.getStartDate().toString() : null;
            this.expectedEndDate = project.getExpectedEndDate() != null ? project.getExpectedEndDate().toString() : null;
            this.lastUpdated = project.getLastUpdated() != null ? project.getLastUpdated().toString() : null;
            this.techStack = techStack;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getCurrentStatus() { return currentStatus; }
        public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

        public Integer getProgressPercentage() { return progressPercentage; }
        public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

        public String getGithubRepo() { return githubRepo; }
        public void setGithubRepo(String githubRepo) { this.githubRepo = githubRepo; }

        public String getLiveDemoUrl() { return liveDemoUrl; }
        public void setLiveDemoUrl(String liveDemoUrl) { this.liveDemoUrl = liveDemoUrl; }

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }

        public String getExpectedEndDate() { return expectedEndDate; }
        public void setExpectedEndDate(String expectedEndDate) { this.expectedEndDate = expectedEndDate; }

        public String getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

        public List<ProjectTechStackDTO> getTechStack() { return techStack; }
        public void setTechStack(List<ProjectTechStackDTO> techStack) { this.techStack = techStack; }
    }

    // Project Tech Stack DTO
    public static class ProjectTechStackDTO {
        private Long id;
        private String technology;
        private String type;

        public ProjectTechStackDTO() {}

        public ProjectTechStackDTO(ProjectTechStack techStack) {
            this.id = techStack.getId();
            this.technology = techStack.getTechnology();
            this.type = techStack.getType();
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTechnology() { return technology; }
        public void setTechnology(String technology) { this.technology = technology; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    // ==================== CONTROLLER METHODS ====================

    // GET ALL STUDENTS WITH THEIR PROJECTS AND TECH STACK
    @GetMapping("/with-projects")
    public ResponseEntity<?> getAllStudentsWithProjects() {
        try {
            System.out.println("📋 Fetching all students with their projects...");

            // 1. Get all students
            List<StudentProfile> students = studentRepo.findAll();
            System.out.println("✅ Found " + students.size() + " students");

            // 2. Get all student IDs
            List<Long> studentIds = students.stream()
                    .map(StudentProfile::getId)
                    .collect(Collectors.toList());

            // 3. Get all projects for these students
            List<StudentProject> allProjects = studentProjectRepository.findByStudentIdIn(studentIds);
            System.out.println("✅ Found " + allProjects.size() + " projects");

            // 4. Group projects by student ID
            Map<Long, List<StudentProject>> projectsByStudentId = allProjects.stream()
                    .collect(Collectors.groupingBy(StudentProject::getStudentId));

            // 5. Get all project IDs
            List<Long> projectIds = allProjects.stream()
                    .map(StudentProject::getId)
                    .collect(Collectors.toList());

            // 6. Get all tech stacks for these projects
            List<ProjectTechStack> allTechStacks = techStackRepository.findByProjectIdIn(projectIds);
            System.out.println("✅ Found " + allTechStacks.size() + " tech stack items");

            // 7. Group tech stacks by project ID
            Map<Long, List<ProjectTechStack>> techStackByProjectId = allTechStacks.stream()
                    .collect(Collectors.groupingBy(ProjectTechStack::getProjectId));

            // 8. Build the response DTOs
            List<StudentWithProjectsDTO> response = new ArrayList<>();

            for (StudentProfile student : students) {
                List<StudentProject> studentProjects = projectsByStudentId.getOrDefault(student.getId(), new ArrayList<>());

                // Convert projects to DTOs with their tech stacks
                List<StudentProjectDTO> projectDTOs = new ArrayList<>();
                for (StudentProject project : studentProjects) {
                    List<ProjectTechStack> projectTechStacks = techStackByProjectId.getOrDefault(project.getId(), new ArrayList<>());

                    List<ProjectTechStackDTO> techStackDTOs = projectTechStacks.stream()
                            .map(ProjectTechStackDTO::new)
                            .collect(Collectors.toList());

                    projectDTOs.add(new StudentProjectDTO(project, techStackDTOs));
                }

                response.add(new StudentWithProjectsDTO(student, projectDTOs));
            }

            System.out.println("✅ Response prepared with " + response.size() + " students");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET SINGLE STUDENT WITH THEIR PROJECTS
    @GetMapping("/{id}/with-projects")
    public ResponseEntity<?> getStudentWithProjects(@PathVariable Long id) {
        try {
            System.out.println("📋 Fetching student ID " + id + " with projects...");

            // 1. Get the student
            StudentProfile student = studentRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

            // 2. Get all projects for this student
            List<StudentProject> studentProjects = studentProjectRepository.findByStudentId(id);
            System.out.println("✅ Found " + studentProjects.size() + " projects");

            // 3. Get project IDs
            List<Long> projectIds = studentProjects.stream()
                    .map(StudentProject::getId)
                    .collect(Collectors.toList());

            // 4. Get tech stacks if there are projects
            Map<Long, List<ProjectTechStack>> techStackByProjectId = new HashMap<>();
            if (!projectIds.isEmpty()) {
                List<ProjectTechStack> allTechStacks = techStackRepository.findByProjectIdIn(projectIds);
                System.out.println("✅ Found " + allTechStacks.size() + " tech stack items");

                techStackByProjectId = allTechStacks.stream()
                        .collect(Collectors.groupingBy(ProjectTechStack::getProjectId));
            }

            // 5. Convert projects to DTOs
            List<StudentProjectDTO> projectDTOs = new ArrayList<>();
            for (StudentProject project : studentProjects) {
                List<ProjectTechStack> projectTechStacks = techStackByProjectId.getOrDefault(project.getId(), new ArrayList<>());

                List<ProjectTechStackDTO> techStackDTOs = projectTechStacks.stream()
                        .map(ProjectTechStackDTO::new)
                        .collect(Collectors.toList());

                projectDTOs.add(new StudentProjectDTO(project, techStackDTOs));
            }

            // 6. Build response
            StudentWithProjectsDTO response = new StudentWithProjectsDTO(student, projectDTOs);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    // ================================
    // STUDENTS
    // ================================

    @PostMapping("/create-student")
    @Transactional
    public ResponseEntity<?> createStudent(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("=== Creating Student ===");
            System.out.println("Received payload: " + payload);

            // Extract user data from payload
            Map<String, Object> userData = (Map<String, Object>) payload.get("user");
            if (userData == null) {
                return ResponseEntity.badRequest().body("User data is required");
            }

            // Create User object
            User user = new User();
            user.setUsername((String) userData.get("username"));
            user.setPassword((String) userData.get("password"));
            user.setRole((String) userData.get("role"));

            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword());
            System.out.println("Role: " + user.getRole());

            // Check if username already exists
            Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            // Save user first
            User savedUser = userRepository.save(user);
            System.out.println("User saved with ID: " + savedUser.getId());

            // Create StudentProfile object
            StudentProfile student = new StudentProfile();
            student.setUser(savedUser);
            student.setFullName((String) payload.get("fullName"));
            student.setRollNumber((String) payload.get("rollNumber"));
            student.setRegisterNumber((String) payload.get("registerNumber"));
            student.setBranch((String) payload.get("branch"));

            Object semesterObj = payload.get("semester");
            if (semesterObj != null) {
                student.setSemester(Integer.parseInt(semesterObj.toString()));
            }

            student.setBatch((String) payload.get("batch"));
            student.setSection((String) payload.get("section"));

            Object admissionYearObj = payload.get("admissionYear");
            if (admissionYearObj != null) {
                student.setAdmissionYear(Integer.parseInt(admissionYearObj.toString()));
            }

            student.setCollegeName((String) payload.get("collegeName"));
            student.setUniversity((String) payload.get("university"));
            student.setProfileStatus(ProfileStatus.valueOf("ACTIVE"));

            // Save student profile
            StudentProfile savedStudent = studentRepo.save(student);
            System.out.println("Student profile saved with ID: " + savedStudent.getId());

            return ResponseEntity.ok("Student created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating student: " + e.getMessage());
        }
    }
    // UPDATE STUDENT
    @PutMapping("/students/{id}")
    @Transactional
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateRequest request) {
        try {
            System.out.println("✏️ Updating student ID: " + id);

            StudentProfile student = studentRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

            // Update ONLY the fields that exist in your entity
            if (request.getFullName() != null) {
                student.setFullName(request.getFullName());
            }

            if (request.getRollNumber() != null) {
                student.setRollNumber(request.getRollNumber());
            }

            if (request.getRegisterNumber() != null) {
                student.setRegisterNumber(request.getRegisterNumber());
            }

            if (request.getBranch() != null) {
                student.setBranch(request.getBranch());
            }

            if (request.getSemester() != null) {
                student.setSemester(request.getSemester());
            }

            if (request.getBatch() != null) {
                student.setBatch(request.getBatch());
            }

            if (request.getSection() != null) {
                student.setSection(request.getSection());
            }

            if (request.getAdmissionYear() != null) {
                student.setAdmissionYear(request.getAdmissionYear());
            }

            if (request.getCollegeName() != null) {
                student.setCollegeName(request.getCollegeName());
            }

            if (request.getUniversity() != null) {
                student.setUniversity(request.getUniversity());
            }

            if (request.getProfileStatus() != null) {
                try {
                    ProfileStatus status = ProfileStatus.valueOf(request.getProfileStatus());
                    student.setProfileStatus(status);
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ Invalid status: " + request.getProfileStatus());
                }
            }

            StudentProfile updated = studentRepo.save(student);
            System.out.println("✅ Student updated: " + updated.getId());

            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/create-teacher")
    @Transactional
    public ResponseEntity<?> createTeacher(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("=== Creating Teacher ===");
            System.out.println("Received payload: " + payload);

            // Extract user data from payload
            Map<String, Object> userData = (Map<String, Object>) payload.get("user");
            if (userData == null) {
                return ResponseEntity.badRequest().body("User data is required");
            }

            // Create User object
            User user = new User();
            user.setUsername((String) userData.get("username"));
            user.setPassword((String) userData.get("password"));
            user.setRole((String) userData.get("role"));

            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword());
            System.out.println("Role: " + user.getRole());

            // Check if username already exists
            Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            // Save user first
            User savedUser = userRepository.save(user);
            System.out.println("User saved with ID: " + savedUser.getId());

            // Create TeacherProfile object with all fields
            TeacherProfile teacher = new TeacherProfile();
            teacher.setUser(savedUser);
            teacher.setFullName((String) payload.get("fullName"));
            teacher.setEmployeeId((String) payload.get("employeeId"));
            teacher.setDepartment((String) payload.get("department"));

            // Additional fields (optional)
            teacher.setEmail((String) payload.get("email"));
            teacher.setPhone((String) payload.get("phone"));
            teacher.setDesignation((String) payload.get("designation"));
            teacher.setQualification((String) payload.get("qualification"));

            Object experienceYearsObj = payload.get("experienceYears");
            if (experienceYearsObj != null) {
                teacher.setExperienceYears(Integer.parseInt(experienceYearsObj.toString()));
            }

            teacher.setBio((String) payload.get("bio"));

            // Save teacher profile
            TeacherProfile savedTeacher = teacherRepo.save(teacher);
            System.out.println("Teacher profile saved with ID: " + savedTeacher.getId());

            return ResponseEntity.ok("Teacher created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating teacher: " + e.getMessage());
        }
    }
    static                                                                                                                                                                                                                                              class StudentUpdateRequest {
        private String fullName;
        private String rollNumber;
        private String registerNumber;
        private String branch;
        private Integer semester;
        private String batch;
        private String section;
        private Integer admissionYear;
        private String collegeName;
        private String university;
        private String profileStatus;

        // Getters and setters
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getRollNumber() { return rollNumber; }
        public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

        public String getRegisterNumber() { return registerNumber; }
        public void setRegisterNumber(String registerNumber) { this.registerNumber = registerNumber; }

        public String getBranch() { return branch; }
        public void setBranch(String branch) { this.branch = branch; }

        public Integer getSemester() { return semester; }
        public void setSemester(Integer semester) { this.semester = semester; }

        public String getBatch() { return batch; }
        public void setBatch(String batch) { this.batch = batch; }

        public String getSection() { return section; }
        public void setSection(String section) { this.section = section; }

        public Integer getAdmissionYear() { return admissionYear; }
        public void setAdmissionYear(Integer admissionYear) { this.admissionYear = admissionYear; }

        public String getCollegeName() { return collegeName; }
        public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

        public String getUniversity() { return university; }
        public void setUniversity(String university) { this.university = university; }

        public String getProfileStatus() { return profileStatus; }
        public void setProfileStatus(String profileStatus) { this.profileStatus = profileStatus; }
    }
    // ================================
    // GET ALL TEACHERS (to verify all fields)
    // ================================

    @GetMapping("/teachers")
    public List<TeacherProfile> getAllTeachers() {
        return teacherRepo.findAll();
    }

    @GetMapping("/students")
    public List<StudentProfile> getAllStudents() {
        return studentRepo.findAll();
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        studentRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Student deleted"));
    }

//    @DeleteMapping("/teacher/{id}")
//    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
//        teacherRepo.deleteById(id);
//        return ResponseEntity.ok(Map.of("message", "Teacher deleted"));
//    }
    @DeleteMapping("/teacher/{id}")
    @Transactional  // Ensures both operations succeed or fail together
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        try {
            // Log the deletion attempt
            System.out.println("🔍 Attempting to delete teacher with ID: " + id);

            // Step 1: Find the teacher profile
            TeacherProfile teacher = teacherRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

            // Step 2: Get the associated user
            User user = teacher.getUser();
            if (user == null) {
                throw new RuntimeException("Teacher has no associated user account");
            }

            Long userId = user.getId();
            String username = user.getUsername();

            System.out.println("📋 Teacher found: " + teacher.getFullName());
            System.out.println("📋 Associated user: " + username + " (ID: " + userId + ")");

            // Step 3: Delete teacher profile first (foreign key constraint)
            teacherRepo.deleteById(id);
            System.out.println("✅ Teacher profile deleted from teacher_profiles table");

            // Step 4: Delete the user
            userRepository.deleteById(userId);
            System.out.println("✅ User account deleted from users table");

            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Teacher and associated user account deleted successfully");
            response.put("teacherId", id);
            response.put("userId", userId);
            response.put("username", username);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error deleting teacher: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to delete teacher");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    @GetMapping("/materials")
    public List<Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @DeleteMapping("/material/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok(Map.of("message", "Material deleted"));
    }

    @GetMapping("/projects")
    public List<StudentProject> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/quizzes")
    public ResponseEntity<?> getAllQuizzes() {
        return ResponseEntity.ok(teacherQuizService.getAllQuizzes());
    }

    @DeleteMapping("/quiz/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long quizId) {
        teacherQuizService.deleteQuiz(quizId);
        return ResponseEntity.ok(Map.of("message", "Quiz deleted"));
    }

    @GetMapping("/stats")
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("students", studentRepo.count());
        stats.put("teachers", teacherRepo.count());
        stats.put("materials", materialService.getAllMaterials().size());
        stats.put("projects", projectService.getAllProjects().size());
        stats.put("quizzes", teacherQuizService.getAllQuizzes().size());
        return stats;
    }

    @GetMapping("/test")
    public String test() {
        return "Admin API working!";
    }
}