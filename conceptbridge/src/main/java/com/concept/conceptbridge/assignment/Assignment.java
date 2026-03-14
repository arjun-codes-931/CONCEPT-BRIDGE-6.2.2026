package com.concept.conceptbridge.assignment;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String subject;
    private String branch;
    private Integer semester;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "max_marks")
    private Integer maxMarks;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "created_by")
    private Long createdBy;  // teacher_id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setMaxMarks(Integer maxMarks) {
        this.maxMarks = maxMarks;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSubject() { return subject; }
    public String getBranch() { return branch; }
    public Integer getSemester() { return semester; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Integer getMaxMarks() { return maxMarks; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public Long getCreatedBy() {
        return createdBy;
    }
}