package com.concept.conceptbridge.student;

import com.concept.conceptbridge.entity.ProfileStatus;
import com.concept.conceptbridge.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "student_profiles",
        uniqueConstraints = @UniqueConstraint(columnNames = "user_id")
)
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @JsonIgnore
    private User user;

    // ===== Personal (student editable) =====
    private String fullName;

    // ===== Academic (admin only) =====
    private String rollNumber;
    private String registerNumber;
    private String branch;
    private Integer semester;
    private String batch;
    private String section;
    private Integer admissionYear;
    private String collegeName;
    private String university;

    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudentProfile() {}

    // ✅ ADMIN constructor
    public StudentProfile(
            User user,
            String rollNumber,
            String registerNumber,
            String branch,
            Integer semester,
            String batch,
            String section,
            Integer admissionYear,
            String collegeName,
            String university
    ) {
        this.user = user;
        this.rollNumber = rollNumber;
        this.registerNumber = registerNumber;
        this.branch = branch;
        this.semester = semester;
        this.batch = batch;
        this.section = section;
        this.admissionYear = admissionYear;
        this.collegeName = collegeName;
        this.university = university;
        this.profileStatus = ProfileStatus.ACTIVE;
    }

    // ✅ STUDENT-ONLY update
    public void updatePersonalInfo(String fullName) {
        this.fullName = fullName;
        this.profileStatus = ProfileStatus.ACTIVE;
    }

    public void deactivate() {
        this.profileStatus = ProfileStatus.SUSPENDED;
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

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public void setAdmissionYear(Integer admissionYear) {
        this.admissionYear = admissionYear;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // ===== GETTERS ONLY =====
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getFullName() { return fullName; }
    public String getRollNumber() { return rollNumber; }
    public String getRegisterNumber() { return registerNumber; }
    public String getBranch() { return branch; }
    public Integer getSemester() { return semester; }
    public String getBatch() { return batch; }
    public String getSection() { return section; }
    public Integer getAdmissionYear() { return admissionYear; }
    public String getCollegeName() { return collegeName; }
    public String getUniversity() { return university; }
    public ProfileStatus getProfileStatus() { return profileStatus; }
}
