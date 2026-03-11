package com.concept.conceptbridge.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private StudentProjectRepository projectRepository;

    @Autowired
    private ProjectTechStackRepository techStackRepository;

    // Get all projects for student
    public List<StudentProject> getStudentProjects(Long studentId) {
        return projectRepository.findByStudentId(studentId);
    }

    public Optional<StudentProject> getProjectStudent(Long PIds) {
        return projectRepository.findById(PIds);
    }

    // Create new project with tech stack
    @Transactional
    public StudentProject createProject(Long studentId, ProjectRequest request) {
        // Create project
        StudentProject project = new StudentProject();
        project.setStudentId(studentId);
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setCategory(request.getCategory());

        // Use the status from the request instead of hardcoding "IDEA"
        project.setCurrentStatus(request.getCurrentStatus() != null ?
                request.getCurrentStatus() : "IDEA");

        // Use the progress from the request instead of hardcoding 0
        project.setProgressPercentage(request.getProgressPercentage() != null ?
                request.getProgressPercentage() : 0);

        project.setGithubRepo(request.getGithubRepo());
        project.setLiveDemoUrl(request.getLiveDemoUrl());
        project.setStartDate(LocalDate.now());
        project.setExpectedEndDate(request.getExpectedEndDate());

        StudentProject savedProject = projectRepository.save(project);

        // Save tech stack
        if (request.getTechStack() != null) {
            for (TechStackItem tech : request.getTechStack()) {
                ProjectTechStack techStack = new ProjectTechStack();
                techStack.setProjectId(savedProject.getId());
                techStack.setTechnology(tech.getTechnology());
                techStack.setType(tech.getType());
                techStackRepository.save(techStack);
            }
        }

        return savedProject;
    }

    // Delete project
    @Transactional
    public void deleteProject(Long projectId) {
        // Delete tech stack first (cascade)
        techStackRepository.deleteByProjectId(projectId);
        // Delete project
        projectRepository.deleteById(projectId);
    }

    // Update project
    @Transactional
    public StudentProject updateProject(Long projectId, ProjectRequest request) {
        StudentProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setCategory(request.getCategory());
        project.setCurrentStatus(request.getCurrentStatus());
        project.setProgressPercentage(request.getProgressPercentage());
        project.setGithubRepo(request.getGithubRepo());
        project.setLiveDemoUrl(request.getLiveDemoUrl());
        project.setExpectedEndDate(request.getExpectedEndDate());
        project.setLastUpdated(LocalDateTime.now());

        // Update tech stack
        techStackRepository.deleteByProjectId(projectId);
        if (request.getTechStack() != null) {
            for (TechStackItem tech : request.getTechStack()) {
                ProjectTechStack techStack = new ProjectTechStack();
                techStack.setProjectId(projectId);
                techStack.setTechnology(tech.getTechnology());
                techStack.setType(tech.getType());
                techStackRepository.save(techStack);
            }
        }

        return projectRepository.save(project);
    }

    public List<StudentProject> getAllProjects() {
        return projectRepository.findAll();
    }

    // Get project with tech stack - FIXED RESPONSE
    public ProjectResponse getProjectWithTech(Long projectId) {
        StudentProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<ProjectTechStack> techStack = techStackRepository.findByProjectId(projectId);

        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setStudentId(project.getStudentId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setCategory(project.getCategory());
        response.setCurrentStatus(project.getCurrentStatus());
        response.setProgressPercentage(project.getProgressPercentage());
        response.setGithubRepo(project.getGithubRepo());
        response.setLiveDemoUrl(project.getLiveDemoUrl());
        response.setStartDate(project.getStartDate());
        response.setExpectedEndDate(project.getExpectedEndDate());
        response.setLastUpdated(project.getLastUpdated());
        response.setTechStack(techStack);

        return response;
    }

    // Request/Response DTOs (inner classes)
    public static class ProjectRequest {
        private String title;
        private String description;
        private String category;
        private String currentStatus;
        private Integer progressPercentage;
        private String githubRepo;
        private String liveDemoUrl;
        private LocalDate expectedEndDate;
        private List<TechStackItem> techStack;

        // Getters and setters
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
        public LocalDate getExpectedEndDate() { return expectedEndDate; }
        public void setExpectedEndDate(LocalDate expectedEndDate) { this.expectedEndDate = expectedEndDate; }
        public List<TechStackItem> getTechStack() { return techStack; }
        public void setTechStack(List<TechStackItem> techStack) { this.techStack = techStack; }
    }

    public static class TechStackItem {
        private String technology;
        private String type;

        public String getTechnology() { return technology; }
        public void setTechnology(String technology) { this.technology = technology; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    // FIXED ProjectResponse - NOW MATCHES YOUR ENTITY STRUCTURE
    public static class ProjectResponse {
        private Long id;
        private Long studentId;
        private String title;
        private String description;
        private String category;
        private String currentStatus;
        private Integer progressPercentage;
        private String githubRepo;
        private String liveDemoUrl;
        private LocalDate startDate;
        private LocalDate expectedEndDate;
        private LocalDateTime lastUpdated;
        private List<ProjectTechStack> techStack;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }

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

        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

        public LocalDate getExpectedEndDate() { return expectedEndDate; }
        public void setExpectedEndDate(LocalDate expectedEndDate) { this.expectedEndDate = expectedEndDate; }

        public LocalDateTime getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

        public List<ProjectTechStack> getTechStack() { return techStack; }
        public void setTechStack(List<ProjectTechStack> techStack) { this.techStack = techStack; }
    }
}