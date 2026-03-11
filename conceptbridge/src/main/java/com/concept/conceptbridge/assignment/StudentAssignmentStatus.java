package com.concept.conceptbridge.assignment;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_assignment_status")
public class StudentAssignmentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "assignment_id")
    private Long assignmentId;

    private String status;  // NOT_VIEWED / VIEWED / SUBMITTED

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "marks_obtained")
    private Integer marksObtained;

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setMarksObtained(Integer marksObtained) {
        this.marksObtained = marksObtained;
    }

    // Getters
    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Long getAssignmentId() { return assignmentId; }
    public String getStatus() { return status; }
    public LocalDateTime getViewedAt() { return viewedAt; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public Integer getMarksObtained() { return marksObtained; }
}