package com.concept.conceptbridge.material;


import jakarta.persistence.*;

@Entity
@Table(name = "student_pdf_progress")
public class StudentPDFProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "pdf_id")
    private Long pdfId;

    @Column(name = "last_page_read")
    private Integer lastPageRead;

    @Column(name = "completion_percentage")
    private Integer completionPercentage;

    @Column(name = "last_accessed")
    private java.time.LocalDateTime lastAccessed;

    // Getters only
    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Long getPdfId() { return pdfId; }
    public Integer getLastPageRead() { return lastPageRead; }
    public Integer getCompletionPercentage() { return completionPercentage; }
    public java.time.LocalDateTime getLastAccessed() { return lastAccessed; }
}