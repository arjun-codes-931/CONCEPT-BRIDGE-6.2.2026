package com.concept.conceptbridge.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentProjectRepository extends JpaRepository<StudentProject, Long> {
    List<StudentProject> findByStudentId(Long studentId);
    List<StudentProject> findByStudentIdAndCategory(Long studentId, String category);

    List<StudentProject> findByStudentIdIn(List<Long> studentIds);
}