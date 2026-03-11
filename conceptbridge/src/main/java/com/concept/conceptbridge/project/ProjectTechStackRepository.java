package com.concept.conceptbridge.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {
    List<ProjectTechStack> findByProjectId(Long projectId);
    void deleteByProjectId(Long projectId);
    List<ProjectTechStack> findByProjectIdIn(List<Long> projectIds);

}