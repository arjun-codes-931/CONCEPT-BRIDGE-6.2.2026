package com.concept.conceptbridge.teacher;

import com.concept.conceptbridge.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "teacher_profiles",
        uniqueConstraints = @UniqueConstraint(columnNames = "user_id")
)
public class TeacherProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @JsonIgnore
    private User user;  // MUST have getter getUser()

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId;

    private String email;
    private String phone;

    @Column(nullable = false)
    private String department;

    private String designation;
    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    private String bio;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public TeacherProfile() {}

    public TeacherProfile(User user, String fullName, String employeeId, String department) {
        this.user = user;
        this.fullName = fullName;
        this.employeeId = employeeId;
        this.department = department;
        this.createdAt = LocalDateTime.now();
    }

    // Update method
    public void updatePersonalInfo(String fullName) {
        this.fullName = fullName;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ===== GETTERS ONLY =====
    public Long getId() { return id; }
    public User getUser() { return user; }  // CRITICAL: Must have getUser()
    public String getFullName() { return fullName; }
    public String getEmployeeId() { return employeeId; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDepartment() { return department; }
    public String getDesignation() { return designation; }
    public String getQualification() { return qualification; }
    public Integer getExperienceYears() { return experienceYears; }
    public String getBio() { return bio; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}