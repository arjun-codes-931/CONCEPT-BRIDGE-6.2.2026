package com.concept.conceptbridge.material;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pdf_materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String subject;
    private String branch;
    private Integer semester;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "total_pages")
    private Integer totalPages;

    private String visibility;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructor
    public Material() {
        this.createdAt = LocalDateTime.now();
    }

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

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getters only
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSubject() { return subject; }
    public String getBranch() { return branch; }
    public Integer getSemester() { return semester; }
    public Long getUploadedBy() { return uploadedBy; }
    public String getFileUrl() { return fileUrl; }
    public Integer getTotalPages() { return totalPages; }
    public String getVisibility() { return visibility; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}