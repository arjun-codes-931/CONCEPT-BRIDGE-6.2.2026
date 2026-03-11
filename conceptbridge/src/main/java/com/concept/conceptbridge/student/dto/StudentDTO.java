package com.concept.conceptbridge.student.dto;

public class StudentDTO {
    private Long id;
    private Long userId;  // Add this
    private String fullName;
    private String rollNumber;
    private String branch;
    private Integer semester;

    // Update constructor
    public StudentDTO(Long id, Long userId, String fullName, String rollNumber,
                      String branch, Integer semester) {
        this.id = id;
        this.userId = userId;  // Add this
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.branch = branch;
        this.semester = semester;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }  // Add this getter
    public String getFullName() { return fullName; }
    public String getRollNumber() { return rollNumber; }
    public String getBranch() { return branch; }
    public Integer getSemester() { return semester; }
}