package com.concept.conceptbridge.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentProfileUpdateRequest {

    @NotBlank
    private String fullName;

    private String rollNumber;
    private String registerNumber;

    @NotBlank
    private String branch;

    @NotNull
    private Integer semester;

    private String batch;
    private String section;
    private Integer admissionYear;

    private String collegeName;
    private String university;

    /* Getters only */

    public String getFullName() {
        return fullName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public String getBranch() {
        return branch;
    }

    public Integer getSemester() {
        return semester;
    }

    public String getBatch() {
        return batch;
    }

    public String getSection() {
        return section;
    }

    public Integer getAdmissionYear() {
        return admissionYear;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getUniversity() {
        return university;
    }
}
