package com.concept.conceptbridge.project;

import jakarta.persistence.*;


@Entity
@Table(name = "project_tech_stack")
public class ProjectTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    private String technology;
    private String type;  // BACKEND / FRONTEND / DB / OTHER

    public void setId(Long id) {
        this.id = id;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getters
    public Long getId() { return id; }
    public Long getProjectId() { return projectId; }
    public String getTechnology() { return technology; }
    public String getType() { return type; }
}